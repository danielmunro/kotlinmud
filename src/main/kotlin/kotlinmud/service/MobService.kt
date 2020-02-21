package kotlinmud.service

import kotlinmud.attributes.Attribute
import kotlinmud.event.EventResponse
import kotlinmud.event.createSendMessageToRoomEvent
import kotlinmud.event.event.SendMessageToRoomEvent
import kotlinmud.io.Message
import kotlinmud.mob.Disposition
import kotlinmud.mob.Mob
import kotlinmud.mob.fight.Attack
import kotlinmud.mob.fight.AttackResult
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.MobRoom
import kotlinmud.mob.fight.Round
import kotlinmud.room.Room

class MobService(private val eventService: EventService, private val rooms: List<Room>) {
    private val mobs: List<Mob> = listOf()
    private val mobRooms: MutableList<MobRoom> = mutableListOf()
    private val fights: MutableList<Fight> = mutableListOf()

    fun addFight(fight: Fight) {
        fights.add(fight)
    }

    fun getRoomForMob(mob: Mob): Room {
        return mobRooms.find { it.mob == mob }!!.room
    }

    fun getMobsForRoom(room: Room): List<Mob> {
        return mobRooms.filter { it.room == room }.map { it.mob }
    }

    fun respawnMobToStartRoom(mob: Mob) {
        // @todo: copy mob object, don't use reference
        putMobInRoom(mob, rooms[0])
    }

    fun moveMob(mob: Mob, room: Room) {
        putMobInRoom(mob, room)
    }

    fun proceedFights(): List<Round> {
        val rounds = fights.map { fight ->
            val round = fight.createRound()
            val room = getRoomForMob(round.attacker)
            applyRoundDamage(round.attackerAttacks, round.defender)
            sendRoundMessage(round.attackerAttacks, room, round.attacker, round.defender)
            if (round.defender.isStanding()) {
                applyRoundDamage(round.defenderAttacks, round.attacker)
                sendRoundMessage(round.defenderAttacks, room, round.defender, round.attacker)
            }
            eventService.publish<SendMessageToRoomEvent, EventResponse<SendMessageToRoomEvent>>(
                createSendMessageToRoomEvent(
                    Message(getHealthIndication(round.defender)),
                    room,
                    round.attacker
                )
            )
            eventService.publish<SendMessageToRoomEvent, EventResponse<SendMessageToRoomEvent>>(
                createSendMessageToRoomEvent(
                    Message(getHealthIndication(round.attacker)),
                    room,
                    round.defender
                )
            )
            round
        }
        fights.removeIf { it.isOver() }
        return rounds
    }

    fun decrementAffects() {
        mobRooms.forEach { mobRoom ->
            mobRoom.mob.affects.removeIf {
                it.timeout--
                it.timeout < 0
            }
        }
    }

    private fun sendRoundMessage(attacks: List<Attack>, room: Room, attacker: Mob, defender: Mob) {
        attacks.forEach {
            val verb = if (it.attackResult == AttackResult.HIT) "hit" else "miss"
            val verbPlural = if (it.attackResult == AttackResult.HIT) "hits" else "misses"
            eventService.publish<SendMessageToRoomEvent, EventResponse<SendMessageToRoomEvent>>(
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

    private fun applyRoundDamage(attacks: List<Attack>, mob: Mob) {
        attacks.forEach {
            if (it.attackResult == AttackResult.HIT) {
                mob.hp -= it.damage
            }
        }
        if (mob.hp < 0) {
            mob.disposition = Disposition.DEAD
        }
    }

    private fun putMobInRoom(mob: Mob, room: Room) {
        val r = rooms.find { it == room }
        if (r == null) {
            println("no room exists")
            return
        }

        val mobRoom = mobRooms.find { it.mob == mob }
        if (mobRoom == null) {
            mobRooms.add(MobRoom(mob, r))
            return
        }

        mobRoom.room = r
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
