package kotlinmud.mob.service

import com.cesarferreira.pluralize.pluralize
import kotlinmud.affect.table.Affects
import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.type.Attribute
import kotlinmud.biome.type.BiomeType
import kotlinmud.event.factory.createKillEvent
import kotlinmud.event.factory.createSendMessageToRoomEvent
import kotlinmud.event.impl.Event
import kotlinmud.event.service.EventService
import kotlinmud.event.type.EventType
import kotlinmud.helper.logger
import kotlinmud.helper.math.normalizeDouble
import kotlinmud.io.factory.createArriveMessage
import kotlinmud.io.factory.createDeathMessage
import kotlinmud.io.factory.createFleeMessage
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
import kotlinmud.mob.helper.takeDamageFromFall
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.table.Mobs
import kotlinmud.mob.type.Disposition
import kotlinmud.player.dao.MobCardDAO
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
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class MobService(
    private val itemService: ItemService,
    private val eventService: EventService
) {
    private val newRooms = mutableListOf<NewRoom>()
    private val fights = mutableListOf<Fight>()
    private val logger = logger(this)

    fun regenMobs() {
        transaction {
            MobDAO.wrapRows(Mobs.select { Mobs.isNpc eq false }).forEach {
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

    fun moveMob(mob: MobDAO, destinationRoom: RoomDAO, directionLeavingFrom: Direction) {
        val leaving = transaction { mob.room }
        sendMessageToRoom(createLeaveMessage(mob, directionLeavingFrom), leaving, mob)
        transaction { mob.room = destinationRoom }
        doFallCheck(mob, leaving, destinationRoom)
        sendMessageToRoom(createArriveMessage(mob), destinationRoom, mob)
    }

    fun proceedFights(): List<Round> {
        return createNewFightRounds().also {
            fights.filter { it.hasFatality() }.forEach {
                eventService.publish(createKillEvent(it))
                fights.remove(it)
            }
        }
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

    fun flee(mob: MobDAO) {
        fights.find { it.isParticipant(mob) }?.let {
            it.end()
            transaction {
                mob.room.getAllExits().entries.random().let { exit ->
                    sendMessageToRoom(
                        createFleeMessage(mob, exit.key),
                        mob.room,
                        mob
                    )
                    mob.room = exit.value
                    sendMessageToRoom(
                        createArriveMessage(mob),
                        exit.value,
                        mob
                    )
                }
            }
        } ?: logger.debug("flee :: no fight for mob :: {}", mob.id)
    }

    fun transferGold(src: MobDAO, dst: MobDAO, amount: Int = src.gold) {
        transaction {
            src.gold -= amount
            dst.gold += amount
        }
    }

    fun train(card: MobCardDAO, attribute: Attribute) {
        transaction {
            card.trains -= 1
            val attributes = AttributesDAO.new {
                mobCard = card
            }
            attributes.setAttribute(attribute, if (attribute.isVitals()) 10 else 1)
        }
    }

    fun practice(mob: MobDAO, skillType: SkillType) {
        transaction {
            mob.mobCard?.let { it.practices -= 1 }
            mob.skills.find { it.type == skillType }?.let { it.level += 1 }
        }
    }

    private fun createNewFightRounds(): List<Round> {
        return fights.map {
            proceedFightRound(it.createRound()).also { round ->
                eventService.publish(Event(EventType.FIGHT_ROUND, round))
            }
        }
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
