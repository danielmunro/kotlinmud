package kotlinmud.service

import kotlinmud.attributes.Attribute
import kotlinmud.event.createSendMessageToRoomEvent
import kotlinmud.io.Message
import kotlinmud.mob.Mob
import kotlinmud.mob.MobRoom
import kotlinmud.mob.fight.Attack
import kotlinmud.mob.fight.AttackResult
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.fight.Round
import kotlinmud.room.Room

class MobService(private val eventService: EventService, private val rooms: List<Room>) {
    private val mobRooms: MutableList<MobRoom> = mutableListOf()
    private val fights: MutableList<Fight> = mutableListOf()

    fun addFight(fight: Fight) {
        fights.add(fight)
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

    fun respawnMobToStartRoom(mob: Mob) {
        putMobInRoom(mob, rooms[0])
    }

    fun moveMob(mob: Mob, room: Room) {
        putMobInRoom(mob, room)
    }

    fun proceedFights(): List<Round> {
        val rounds = fights.map { proceedFightRound(it.createRound()) }
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
