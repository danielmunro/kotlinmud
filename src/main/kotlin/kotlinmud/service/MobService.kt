package kotlinmud.service

import kotlinmud.mob.fight.Attack
import kotlinmud.mob.fight.AttackResult
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.MobEntity
import kotlinmud.mob.MobRoom
import kotlinmud.room.RoomEntity
import org.jetbrains.exposed.sql.transactions.transaction

class MobService(private val rooms: List<RoomEntity>) {
    private val mobs: List<MobEntity> = listOf()
    private val mobRooms: MutableList<MobRoom> = mutableListOf()
    private val fights: MutableList<Fight> = mutableListOf()

    fun getRoomForMob(mob: MobEntity): RoomEntity {
        return mobRooms.find { it.mob == mob }!!.room
    }

    fun getMobsForRoom(room: RoomEntity): List<MobEntity> {
        return mobRooms.filter { it.room.uuid == room.uuid }.map { it.mob }
    }

    fun respawnMobToStartRoom(mob: MobEntity) {
        // @todo: copy mob object, don't use reference
        putMobInRoom(mob, rooms[0])
    }

    fun moveMob(mob: MobEntity, room: RoomEntity) {
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

    private fun applyRoundDamage(attacks: List<Attack>, mob: MobEntity) {
        attacks.forEach {
            if (it.attackResult == AttackResult.HIT) {
                mob.hp -= it.damage
            }
        }
    }

    private fun putMobInRoom(mob: MobEntity, room: RoomEntity) {
        val r = rooms.find { it.uuid == room.uuid }
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
