package kotlinmud.service

import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.event.FightRoundEvent
import kotlinmud.mob.Disposition
import kotlinmud.mob.Mob
import kotlinmud.mob.fight.Attack
import kotlinmud.mob.fight.AttackResult
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.MobRoom
import kotlinmud.mob.fight.Round
import kotlinmud.room.Room
import org.jetbrains.exposed.sql.transactions.transaction

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
            applyRoundDamage(round.attackerAttacks, round.defender)
            if (round.defender.isStanding()) {
                applyRoundDamage(round.defenderAttacks, round.attacker)
            }
//            eventService.publish<FightRoundEvent, Round>(Event(EventType.FIGHT_ROUND, FightRoundEvent(round)))
            round
        }
        fights.removeIf { it.isOver() }
        return rounds
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
