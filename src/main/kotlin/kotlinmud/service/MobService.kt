package kotlinmud.service

import kotlinmud.attributes.Attribute
import kotlinmud.event.EventResponse
import kotlinmud.event.createSendMessageToRoomEvent
import kotlinmud.event.event.SendMessageToRoomEvent
import kotlinmud.io.Message
import kotlinmud.loader.World
import kotlinmud.loader.model.reset.MobReset
import kotlinmud.mob.Mob
import kotlinmud.mob.MobRoom
import kotlinmud.mob.fight.Attack
import kotlinmud.mob.fight.AttackResult
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.fight.Round
import kotlinmud.room.Direction
import kotlinmud.room.Room

class MobService(
    private val eventService: EventService,
    private val world: World
) {

    private val mobRooms: MutableList<MobRoom> = mutableListOf()
    private val fights: MutableList<Fight> = mutableListOf()

    fun getStartRoom(): Room {
        return world.rooms.first()
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
        putMobInRoom(mob, world.rooms[0])
    }

    fun moveMob(mob: Mob, room: Room, direction: Direction) {
        sendMessageToRoom(createLeaveMessage(mob, direction), getRoomForMob(mob), mob)
        putMobInRoom(mob, room)
        sendMessageToRoom(createArriveMessage(mob), room, mob)
    }

    fun proceedFights(): List<Round> {
        fights.removeIf { it.isOver() }
        return fights.map { proceedFightRound(it.createRound()) }
    }

    fun decrementAffects() {
        mobRooms.forEach { mobRoom ->
            mobRoom.mob.affects.removeIf {
                it.timeout--
                it.timeout < 0
            }
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
                it.room.inventory.items.add(it.mob.createCorpse())
                eventService.publishRoomMessage(createSendMessageToRoomEvent(
                    Message("you are DEAD!", "${it.mob} has died!"),
                    it.room,
                    it.mob
                ))
            }
            it.mob.isIncapacitated()
        }
    }

    fun respawnWorld() {
        println("respawn world with ${world.mobResets.size} mob resets")
        world.mobResets.forEach { reset ->
            val room = world.rooms.find { it.id == reset.roomId }!!
            while (mobCanRespawn(reset, room)) {
                val mob = world.mobs.find { it.id == reset.mobId }!!
                world.itemMobResets.filter { it.mobId == mob.id }.forEach { itemReset ->
                    println("FOUND ITEM MOB RESET")
                    val item = world.items.find { it.id == itemReset.itemId }!!
                    mob.inventory.items.add(item.copy())
                    println("mob $mob received $item, inv: ${mob.inventory.items.size}")
                }
                putMobInRoom(mob.copy(), room)
            }
        }
    }

    fun sendMessageToRoom(message: Message, room: Room, actionCreator: Mob, target: Mob? = null) {
        eventService.publish<SendMessageToRoomEvent, EventResponse<SendMessageToRoomEvent>>(
            createSendMessageToRoomEvent(message, room, actionCreator, target))
    }

    private fun mobCanRespawn(reset: MobReset, room: Room): Boolean {
        return reset.maxInRoom > getMobsForRoom(room).filter { mob -> mob.id == reset.mobId }.size && reset.maxInWorld > mobRooms.filter { mobRoom -> mobRoom.mob.id == reset.mobId }.size
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
            val verb = if (it.attackResult == AttackResult.HIT) "hit" else "miss"
            val verbPlural = if (it.attackResult == AttackResult.HIT) "hits" else "misses"
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

    private fun putMobInRoom(mob: Mob, room: Room) {
        val r = world.rooms.find { it == room } ?: error("no room found")

        mobRooms.find { it.mob == mob }?.let {
            it.room = r
        } ?: mobRooms.add(MobRoom(mob, r))
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
