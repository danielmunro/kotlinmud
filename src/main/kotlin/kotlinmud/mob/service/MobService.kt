package kotlinmud.mob.service

import com.cesarferreira.pluralize.pluralize
import java.lang.RuntimeException
import kotlinmud.affect.table.Affects
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
import kotlinmud.mob.fight.AttackResult
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.fight.Round
import kotlinmud.mob.helper.getDispositionRegenRate
import kotlinmud.mob.helper.getRoomRegenRate
import kotlinmud.mob.model.MobRoom
import kotlinmud.mob.table.Mobs
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.helper.oppositeDirection
import kotlinmud.room.model.NewRoom
import kotlinmud.room.table.Rooms
import kotlinmud.room.type.Direction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.minus
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
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
            mob.hp -= damage
        }
    }

    private val mobRooms = mutableListOf<MobRoom>()
    private val newRooms = mutableListOf<NewRoom>()
    private val fights = mutableListOf<Fight>()
    private val logger = logger(this)

    fun regenMobs() {
        logger.debug("regen mobs :: ${mobRooms.size} mobs")
        mobRooms.filter { !it.mob.isIncapacitated() }.forEach {
            it.mob.increaseByRegenRate(
                normalizeDouble(
                    0.0,
                    getRoomRegenRate(it.room.regenLevel) +
                            getDispositionRegenRate(it.mob.disposition),
                    1.0
                )
            )
        }
    }

    fun createNewRoom(mob: MobDAO): NewRoom {
        logger.debug("create new room :: $mob")
        val room = transaction {
            RoomDAO.new {}
        }
        return mobRooms.find { it.mob == mob }?.let {
            val newRoom = NewRoom(it, room)
            newRooms.add(newRoom)
            newRoom
        } ?: throw RuntimeException("mob must be in a room to add a room")
    }

    fun buildRoom(mob: MobDAO, direction: Direction): RoomDAO {
        // setup
        val mobRoom = mobRooms.find { it.mob == mob }!!
        val newRoom = newRooms.find { it.mobRoom == mobRoom }!!

        // destination room exit hook up
        val source = mobRoom.room
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
        val mobRoom = mobRooms.find { it.mob == mob }
        return newRooms.find { mobRoom == it.mobRoom }
    }

    fun getRooms(): List<RoomDAO> {
        return transaction {
            RoomDAO.wrapRows(
                Rooms.selectAll()
            )
        }.toList()
    }

    fun getStartRoom(): RoomDAO {
        return transaction {
            RoomDAO.wrapRow(
                Rooms.select { Rooms.id eq 1 }.first()
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

    fun getRoomForMob(mob: MobDAO): RoomDAO {
        return mobRooms.find { it.mob == mob }!!.room
    }

    fun getMobsForRoom(room: RoomDAO): List<MobDAO> {
        return mobRooms.filter { it.room == room }.map { it.mob }
    }

    fun getMobRooms(): List<MobRoom> {
        return mobRooms
    }

    fun getRoomById(id: Int): RoomDAO? {
        return RoomDAO.wrapRow(
            Rooms.select { Rooms.id eq id }.first()
        )
    }

    fun addMob(mob: MobDAO) {
        putMobInRoom(mob, getStartRoom())
    }

    fun findPlayerMob(name: String): MobDAO? {
        return transaction {
            MobDAO.wrapRow(
                Mobs.select {
                    Mobs.name eq name and (Mobs.isNpc eq false)
                }.first()
            )
        }
    }

    fun moveMob(mob: MobDAO, room: RoomDAO, direction: Direction) {
        val leaving = getRoomForMob(mob)
        sendMessageToRoom(createLeaveMessage(mob, direction), leaving, mob)
        putMobInRoom(mob, room)
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
            Affects.update({ Affects.decay.isNotNull() }) {
                it.update(decay, decay - 1)
            }
            // @todo fix this bug
            // see: https://stackoverflow.com/questions/38779666/how-to-fix-overload-resolution-ambiguity-in-kotlin-no-lambda
            Affects.deleteWhere(9999 as Int, 0 as Int) {
                Affects.decay.isNotNull() and (Affects.decay less 0)
            }
        }
    }

    fun pruneDeadMobs() {
        mobRooms.removeIf {
            if (it.mob.isIncapacitated()) {
                val item = createCorpseFrom(it.mob)
                transaction { item.room = it.room }
                eventService.publishRoomMessage(
                    createSendMessageToRoomEvent(createDeathMessage(it.mob), it.room, it.mob)
                )
            }
            it.mob.isIncapacitated()
        }
    }

    fun removeMob(mob: MobDAO) {
        mobRooms.removeIf { it.mob == mob }
    }

    fun sendMessageToRoom(message: Message, room: RoomDAO, actionCreator: MobDAO, target: MobDAO? = null) {
        eventService.publish(
            createSendMessageToRoomEvent(message, room, actionCreator, target)
        )
    }

    fun putMobInRoom(mob: MobDAO, room: RoomDAO) {
        mobRooms.find { it.mob == mob }?.let {
            it.room = room
        } ?: mobRooms.add(MobRoom(mob, room))
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
        val room = getRoomForMob(round.attacker)
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
