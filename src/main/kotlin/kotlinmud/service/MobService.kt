package kotlinmud.service

import com.cesarferreira.pluralize.pluralize
import java.util.stream.Collectors
import kotlinmud.attributes.Attribute
import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.createSendMessageToRoomEvent
import kotlinmud.io.Message
import kotlinmud.item.Item
import kotlinmud.item.ItemBuilder
import kotlinmud.item.ItemOwner
import kotlinmud.math.normalizeDouble
import kotlinmud.mob.Disposition
import kotlinmud.mob.Mob
import kotlinmud.mob.MobRoom
import kotlinmud.mob.corpseWeight
import kotlinmud.mob.fight.Attack
import kotlinmud.mob.fight.AttackResult
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.fight.Round
import kotlinmud.world.World
import kotlinmud.world.room.Direction
import kotlinmud.world.room.NewRoom
import kotlinmud.world.room.RegenLevel
import kotlinmud.world.room.Room
import kotlinmud.world.room.RoomBuilder
import kotlinmud.world.room.exit.Exit
import kotlinmud.world.room.oppositeDirection

class MobService(
    private val itemService: ItemService,
    private val eventService: EventService,
    private val world: World
) {
    private val mobRooms: MutableList<MobRoom> = mutableListOf()
    private val newRooms: MutableList<NewRoom> = mutableListOf()
    private val fights: MutableList<Fight> = mutableListOf()

    fun regenMobs() {
        mobRooms.filter { !it.mob.isIncapacitated() }.forEach {
            val regen = normalizeDouble(
                0.0,
                getRegenRate(it.room.regen) +
                        getDispositionRegenRate(it.mob.disposition),
                1.0
            )
            it.mob.increaseHp((regen * it.mob.calc(Attribute.HP)).toInt())
            it.mob.increaseMana((regen * it.mob.calc(Attribute.MANA)).toInt())
            it.mob.increaseMv((regen * it.mob.calc(Attribute.MV)).toInt())
        }
    }

    fun createRoomBuilder(mob: Mob): RoomBuilder {
        val roomBuilder = world.createRoomBuilder()
        val mobRoom = mobRooms.find { it.mob == mob }!!
        newRooms.add(NewRoom(mobRoom, roomBuilder))
        return roomBuilder
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

    fun moveMob(mob: Mob, room: Room, direction: Direction) {
        sendMessageToRoom(createLeaveMessage(mob, direction), getRoomForMob(mob), mob)
        putMobInRoom(mob, room)
        sendMessageToRoom(createArriveMessage(mob), room, mob)
    }

    fun proceedFights(): List<Round> {
        val currentFights = fights.filter { !it.isOver() }
        val rounds = currentFights.map {
            proceedFightRound(it.createRound())
        }
        rounds.forEach {
            eventService.publish(Event(EventType.FIGHT_ROUND, it))
        }
        fights.forEach {
            if (it.hasFatality()) {
                eventService.publish(Event(EventType.KILL, it))
            }
        }
        fights.removeIf {
            it.isOver()
        }
        return rounds
    }

    fun decrementAffects() {
        mobRooms.forEach {
            it.mob.affects().decrement()
        }
    }

    fun decrementDelays() {
        mobRooms.forEach { mobRoom ->
            if (mobRoom.mob.delay > 0) {
                mobRoom.mob.delay--
            }
        }
    }

    fun pruneDeadMobs() {
        mobRooms.removeIf {
            if (it.mob.isIncapacitated()) {
                itemService.add(ItemOwner(createCorpseFrom(it.mob), it.room))
                eventService.publishRoomMessage(createSendMessageToRoomEvent(
                    Message("you are DEAD!", "${it.mob} has died!"),
                    it.room,
                    it.mob
                ))
            }
            it.mob.isIncapacitated()
        }
    }

    fun removeMob(mob: Mob) {
        mobRooms.removeIf { it.mob == mob }
    }

    fun sendMessageToRoom(message: Message, room: Room, actionCreator: Mob, target: Mob? = null) {
        eventService.publish(createSendMessageToRoomEvent(message, room, actionCreator, target))
    }

    fun putMobInRoom(mob: Mob, room: Room) {
        mobRooms.find { it.mob == mob }?.let {
            it.room = room
        } ?: mobRooms.add(MobRoom(mob, room))
    }

    fun createCorpseFrom(mob: Mob): Item {
        val corpse = ItemBuilder()
            .id(0)
            .name("a corpse of ${mob.name}")
            .description("a corpse of ${mob.name} is here.")
            .level(mob.level)
            .weight(corpseWeight)
            .inventory(mutableListOf())
            .decayTimer(20)
            .build()

        mob.equipped.stream().collect(Collectors.toList()).forEach {
            mob.equipped.remove(it)
        }

        itemService.transferAllItems(mob, corpse)

        return corpse
    }

    private fun proceedFightRound(round: Round): Round {
        val room = getRoomForMob(round.attacker)
        sendRoundMessage(round.attackerAttacks, room, round.attacker, round.defender)
        sendRoundMessage(round.defenderAttacks, room, round.defender, round.attacker)
        eventService.publishRoomMessage(
            createSendMessageToRoomEvent(
                Message(getHealthIndication(round.defender)),
                room,
                round.attacker
            )
        )
        eventService.publishRoomMessage(
            createSendMessageToRoomEvent(
                Message(getHealthIndication(round.attacker)),
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
                    Message(
                        "you $verb $defender.",
                        "$attacker $verbPlural you.",
                        "$attacker $verbPlural $defender."
                    ),
                    room,
                    attacker,
                    defender
                )
            )
        }
    }
}

fun getHealthIndication(mob: Mob): String {
    val amount: Double = mob.hp.toDouble() / mob.calc(Attribute.HP).toDouble()
    return when {
        amount == 1.0 -> "$mob is in excellent condition."
        amount > 0.9 -> "$mob has a few scratches."
        amount > 0.75 -> "$mob has some small wounds and bruises."
        amount > 0.5 -> "$mob has quite a few wounds."
        amount > 0.3 -> "$mob has some big nasty wounds and scratches."
        amount > 0.15 -> "$mob looks pretty hurt."
        else -> "$mob is in awful condition."
    }
}

fun createLeaveMessage(mob: Mob, direction: Direction): Message {
    return Message(
        "you leave heading ${direction.value}.",
        "${mob.name} leaves heading ${direction.value}.")
}

fun createArriveMessage(mob: Mob): Message {
    return Message(
        "",
        "${mob.name} arrives.")
}

fun getRegenRate(regenLevel: RegenLevel): Double {
    return when (regenLevel) {
        RegenLevel.NONE -> 0.0
        RegenLevel.LOW -> 0.05
        RegenLevel.NORMAL -> 0.1
        RegenLevel.HIGH -> .20
        RegenLevel.FULL -> 1.0
    }
}

fun getDispositionRegenRate(disposition: Disposition): Double {
    return when (disposition) {
        Disposition.DEAD -> 0.0
        Disposition.SLEEPING -> 0.15
        Disposition.SITTING -> 0.05
        Disposition.STANDING -> 0.0
        Disposition.FIGHTING -> -0.15
    }
}
