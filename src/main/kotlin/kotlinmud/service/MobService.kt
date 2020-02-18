package kotlinmud.service

import kotlinmud.mob.Mob
import kotlinmud.mob.fight.Attack
import kotlinmud.mob.fight.AttackResult
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.MobRoom
import kotlinmud.room.Room
import org.jetbrains.exposed.sql.transactions.transaction

class MobService(private val rooms: List<Room>) {
    private val mobs: List<Mob> = listOf()
    private val mobRooms: MutableList<MobRoom> = mutableListOf()
    private val fights: MutableList<Fight> = mutableListOf()

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

    fun proceedFights() {
        fights.forEach { fight ->
            val round = fight.createRound()
            transaction {
                applyRoundDamage(round.attackerAttacks, round.defender)
                if (round.defender.hp > 0) {
                    applyRoundDamage(round.defenderAttacks, round.attacker)
                }
            }
        }
    }

    private fun applyRoundDamage(attacks: List<Attack>, mob: Mob) {
        attacks.forEach {
            if (it.attackResult == AttackResult.HIT) {
                mob.hp -= it.damage
            }
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
