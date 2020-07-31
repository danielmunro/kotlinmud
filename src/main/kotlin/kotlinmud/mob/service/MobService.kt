package kotlinmud.mob.service

import com.cesarferreira.pluralize.pluralize
import kotlinmud.affect.table.Affects
import kotlinmud.biome.type.BiomeType
import kotlinmud.event.factory.createSendMessageToRoomEvent
import kotlinmud.event.impl.Event
import kotlinmud.event.service.EventService
import kotlinmud.event.type.EventType
import kotlinmud.helper.logger
import kotlinmud.helper.math.normalizeDouble
import kotlinmud.io.factory.createArriveMessage
import kotlinmud.io.factory.createDeathMessage
import kotlinmud.io.factory.createLeaveMessage
import kotlinmud.io.factory.createSingleHitMessage
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.model.Message
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.service.ItemService
import kotlinmud.mob.constant.MAX_WALKABLE_ELEVATION
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.fight.Attack
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.fight.Round
import kotlinmud.mob.fight.type.AttackResult
import kotlinmud.mob.helper.getDispositionRegenRate
import kotlinmud.mob.helper.getRoomRegenRate
import kotlinmud.mob.table.Mobs
import kotlinmud.mob.type.Disposition
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.helper.oppositeDirection
import kotlinmud.room.model.NewRoom
import kotlinmud.room.table.Rooms
import kotlinmud.room.type.Direction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.minus
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class MobService(
    private val itemService: ItemService,
    private val eventService: EventService
) {
    companion object {
        private fun takeDamageFromFall(mob: MobDAO, elevationChange: Int) {
            val damage = when {
                elevationChange < MAX_WALKABLE_ELEVATION + 2 -> 3
                elevationChange < MAX_WALKABLE_ELEVATION + 5 -> 10
                elevationChange < MAX_WALKABLE_ELEVATION + 10 -> 50
                else -> elevationChange * 10
            }
            transaction { mob.hp -= damage }
        }
    }

    private val newRooms = mutableListOf<NewRoom>()
    private val fights = mutableListOf<Fight>()
    private val logger = logger(this)

    fun regenMobs() {
        transaction {
            MobDAO.wrapRows(Mobs.selectAll()).forEach {
                it.increaseByRegenRate(
                    normalizeDouble(
                        0.0,
                        getRoomRegenRate(it.room.regenLevel) +
                                getDispositionRegenRate(it.disposition),
                        1.0
                    )
                )
            }
        }
    }

    fun createNewRoom(mob: MobDAO): NewRoom {
        logger.debug("create new room :: $mob")
        val room = transaction {
            RoomDAO.new {}
        }
        val newRoom = NewRoom(mob, room)
        newRooms.add(newRoom)
        return newRoom
    }

    fun buildRoom(mob: MobDAO, direction: Direction): RoomDAO {
        // setup
        val source = transaction { mob.room }
        val newRoom = newRooms.find { it.mob == mob }!!

        // destination room exit hook up
        val destination = newRoom.room
        when (oppositeDirection(direction)) {
            Direction.NORTH -> destination.north = source
            Direction.SOUTH -> destination.south = source
            Direction.EAST -> destination.east = source
            Direction.WEST -> destination.west = source
            Direction.UP -> destination.up = source
            Direction.DOWN -> destination.down = source
        }

        // source room exit hook up
        when (direction) {
            Direction.NORTH -> source.north = destination
            Direction.SOUTH -> source.south = destination
            Direction.EAST -> source.east = destination
            Direction.WEST -> source.west = destination
            Direction.UP -> source.up = destination
            Direction.DOWN -> source.down = destination
        }

        return destination
    }

    fun getNewRoom(mob: MobDAO): NewRoom? {
        return newRooms.find { mob == it.mob }
    }

    fun getStartRoom(): RoomDAO {
        return transaction {
            RoomDAO.wrapRow(
                Rooms.select {
                    Rooms.biome eq BiomeType.ARBOREAL.toString() or
                            (Rooms.biome eq BiomeType.PLAINS.toString()) or
                            (Rooms.biome eq BiomeType.JUNGLE.toString())
                }.limit(1)
                    .first()
            )
        }
    }

    fun addFight(fight: Fight) {
        fights.add(fight)
    }

    fun endFightFor(mob: MobDAO) {
        fights.find { it.isParticipant(mob) }?.end()
    }

    fun findFightForMob(mob: MobDAO): Fight? {
        return fights.find { it.isParticipant(mob) }
    }

    fun getMobsForRoom(room: RoomDAO): List<MobDAO> {
        return transaction {
            MobDAO.wrapRows(
                Mobs.select {
                    Mobs.roomId eq room.id
                }
            ).toList()
        }
    }

    fun getRoomById(id: Int): RoomDAO? {
        return transaction {
            RoomDAO.wrapRow(
                Rooms.select { Rooms.id eq id }.first()
            )
        }
    }

    fun moveMob(mob: MobDAO, room: RoomDAO, direction: Direction) {
        val leaving = transaction { mob.room }
        sendMessageToRoom(createLeaveMessage(mob, direction), leaving, mob)
        transaction { mob.room = room }
        doFallCheck(mob, leaving, room)
        sendMessageToRoom(createArriveMessage(mob), room, mob)
    }

    fun proceedFights(): List<Round> {
        val rounds = fights.filter { !it.isOver() }.map {
            proceedFightRound(it.createRound())
        }
        rounds.forEach {
            eventService.publish(Event(EventType.FIGHT_ROUND, it))
        }
        fights.filter { it.hasFatality() }.forEach {
            eventService.publish(Event(EventType.KILL, it))
        }
        fights.removeIf { it.isOver() }
        return rounds
    }

    fun decrementAffects() {
        transaction {
            // see: https://stackoverflow.com/questions/38779666/how-to-fix-overload-resolution-ambiguity-in-kotlin-no-lambda
            Affects.deleteWhere(null as Int?, null as Int?) {
                Affects.timeout.isNotNull() and (Affects.timeout eq 0)
            }
            Affects.update({ Affects.timeout.isNotNull() }) {
                it.update(timeout, timeout - 1)
            }
        }
    }

    fun pruneDeadMobs() {
        transaction {
            MobDAO.wrapRows(
                Mobs.select {
                    Mobs.disposition eq Disposition.DEAD.toString()
                }
            ).forEach {
                val item = createCorpseFrom(it)
                item.room = it.room
                it.delete()
                eventService.publishRoomMessage(
                    createSendMessageToRoomEvent(createDeathMessage(it), it.room, it)
                )
            }
        }
    }

    fun sendMessageToRoom(message: Message, room: RoomDAO, actionCreator: MobDAO, target: MobDAO? = null) {
        eventService.publish(createSendMessageToRoomEvent(message, room, actionCreator, target))
    }

    fun createCorpseFrom(mob: MobDAO): ItemDAO {
        return itemService.createCorpseFromMob(mob)
    }

    private fun doFallCheck(mob: MobDAO, leaving: RoomDAO, arriving: RoomDAO) {
        (leaving.elevation - arriving.elevation).let {
            if (it > MAX_WALKABLE_ELEVATION) {
                takeDamageFromFall(mob, it)
            }
        }
    }

    private fun proceedFightRound(round: Round): Round {
        val room = transaction { round.attacker.room }
        sendRoundMessage(round.attackerAttacks, room, round.attacker, round.defender)
        sendRoundMessage(round.defenderAttacks, room, round.defender, round.attacker)
        eventService.publishRoomMessage(
            createSendMessageToRoomEvent(
                messageToActionCreator(round.defender.getHealthIndication()),
                room,
                round.attacker
            )
        )
        eventService.publishRoomMessage(
            createSendMessageToRoomEvent(
                messageToActionCreator(round.attacker.getHealthIndication()),
                room,
                round.defender
            )
        )
        return round
    }

    private fun sendRoundMessage(attacks: List<Attack>, room: RoomDAO, attacker: MobDAO, defender: MobDAO) {
        attacks.forEach {
            val verb = if (it.attackResult == AttackResult.HIT) it.attackVerb else "miss"
            val verbPlural = if (it.attackResult == AttackResult.HIT) it.attackVerb.pluralize() else "misses"
            eventService.publishRoomMessage(
                createSendMessageToRoomEvent(
                    createSingleHitMessage(attacker, defender, verb, verbPlural),
                    room,
                    attacker,
                    defender
                )
            )
        }
    }
}
