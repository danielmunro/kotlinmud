package kotlinmud.mob.service

import com.cesarferreira.pluralize.pluralize
import java.lang.RuntimeException
import kotlinmud.event.factory.createSendMessageToRoomEvent
import kotlinmud.event.impl.Event
import kotlinmud.event.service.EventService
import kotlinmud.event.type.EventType
import kotlinmud.fs.factory.playerMobFile
import kotlinmud.helper.logger
import kotlinmud.io.factory.createArriveMessage
import kotlinmud.io.factory.createDeathMessage
import kotlinmud.io.factory.createLeaveMessage
import kotlinmud.io.factory.createSingleHitMessage
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.model.Message
import kotlinmud.item.model.Item
import kotlinmud.item.model.ItemOwner
import kotlinmud.item.service.ItemService
import kotlinmud.math.normalizeDouble
import kotlinmud.mob.fight.Attack
import kotlinmud.mob.fight.AttackResult
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.fight.Round
import kotlinmud.mob.helper.getDispositionRegenRate
import kotlinmud.mob.helper.getRoomRegenRate
import kotlinmud.mob.mapper.mapMob
import kotlinmud.mob.model.MAX_WALKABLE_ELEVATION
import kotlinmud.mob.model.Mob
import kotlinmud.mob.model.MobRoom
import kotlinmud.room.helper.oppositeDirection
import kotlinmud.room.model.Exit
import kotlinmud.room.model.NewRoom
import kotlinmud.room.model.Room
import kotlinmud.room.type.Direction
import kotlinmud.world.model.World

class MobService(
    private val itemService: ItemService,
    private val eventService: EventService,
    private val world: World,
    private val playerMobs: MutableList<Mob>
) {
    companion object {
        private fun takeDamageFromFall(mob: Mob, elevationChange: Int) {
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
                    getRoomRegenRate(it.room.regen) +
                            getDispositionRegenRate(it.mob.disposition),
                    1.0
                )
            )
        }
    }

    fun createNewRoom(mob: Mob): NewRoom {
        logger.debug("create new room :: $mob")
        val roomBuilder = world.createRoomBuilder()
        return mobRooms.find { it.mob == mob }?.let {
            newRooms.add(NewRoom(it, roomBuilder))
            NewRoom(it, roomBuilder)
        } ?: throw RuntimeException("mob must be in a room to add a room")
    }

    fun buildRoom(mob: Mob, direction: Direction): Room {
        // setup
        val mobRoom = mobRooms.find { it.mob == mob }!!
        val newRoom = newRooms.find { it.mobRoom == mobRoom }!!
        val roomBuilder = newRoom.roomBuilder
        val destRoom = roomBuilder.build()

        // destination room exit hook up
        val destExit = Exit(mobRoom.room, oppositeDirection(direction))
        destRoom.exits.add(destExit)

        // source room exit hook up
        val srcExit = Exit(destRoom, direction)
        val srcRoom = mobRoom.room
        srcRoom.exits.add(srcExit)

        // add to world
        world.rooms.add(destRoom)

        return destRoom
    }

    fun getNewRoom(mob: Mob): NewRoom? {
        val mobRoom = mobRooms.find { it.mob == mob }
        return newRooms.find { mobRoom == it.mobRoom }
    }

    fun getRooms(): List<Room> {
        return world.rooms.toList()
    }

    fun getStartRoom(): Room {
        return world.rooms.toList().first()
    }

    fun addFight(fight: Fight) {
        fights.add(fight)
    }

    fun endFightFor(mob: Mob) {
        fights.find { it.isParticipant(mob) }?.end()
    }

    fun findFightForMob(mob: Mob): Fight? {
        return fights.find { it.isParticipant(mob) }
    }

    fun getRoomForMob(mob: Mob): Room {
        return mobRooms.find { it.mob == mob }!!.room
    }

    fun getMobsForRoom(room: Room): List<Mob> {
        return mobRooms.filter { it.room == room }.map { it.mob }
    }

    fun getMobRooms(): List<MobRoom> {
        return mobRooms
    }

    fun addMob(mob: Mob) {
        putMobInRoom(mob, getStartRoom())
    }

    fun addPlayerMob(mob: Mob) {
        playerMobs.add(mob)
    }

    fun findPlayerMob(name: String): Mob? {
        return playerMobs.find { it.name == name }
    }

    fun moveMob(mob: Mob, room: Room, direction: Direction) {
        val leaving = getRoomForMob(mob)
        sendMessageToRoom(createLeaveMessage(mob, direction), leaving, mob)
        putMobInRoom(mob, room)
        (leaving.elevation - room.elevation).let {
            if (it > MAX_WALKABLE_ELEVATION) {
                takeDamageFromFall(mob, it)
            }
        }
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
        mobRooms.forEach {
            it.mob.affects().decrement()
        }
    }

    fun pruneDeadMobs() {
        mobRooms.removeIf {
            if (it.mob.isIncapacitated()) {
                itemService.add(ItemOwner(createCorpseFrom(it.mob), it.room))
                eventService.publishRoomMessage(
                    createSendMessageToRoomEvent(createDeathMessage(it.mob), it.room, it.mob)
                )
            }
            it.mob.isIncapacitated()
        }
    }

    fun removeMob(mob: Mob) {
        mobRooms.removeIf { it.mob == mob }
    }

    fun sendMessageToRoom(message: Message, room: Room, actionCreator: Mob, target: Mob? = null) {
        eventService.publish(
            createSendMessageToRoomEvent(message, room, actionCreator, target)
        )
    }

    fun putMobInRoom(mob: Mob, room: Room) {
        mobRooms.find { it.mob == mob }?.let {
            it.room = room
        } ?: mobRooms.add(MobRoom(mob, room))
    }

    fun createCorpseFrom(mob: Mob): Item {
        return itemService.createCorpseFromMob(mob)
    }

    fun persistPlayerMobs() {
        playerMobFile().writeText(playerMobs.joinToString("\n") { mapMob(it) })
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

    private fun sendRoundMessage(attacks: List<Attack>, room: Room, attacker: Mob, defender: Mob) {
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
