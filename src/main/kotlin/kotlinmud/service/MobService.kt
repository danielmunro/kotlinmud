package kotlinmud.service

import kotlinmud.mob.MobEntity
import kotlinmud.mob.MobRoom
import kotlinmud.room.RoomEntity

class MobService(private val rooms: List<RoomEntity>) {
    private val mobs: List<MobEntity> = listOf()
    private val mobRooms: MutableList<MobRoom> = mutableListOf()

    fun getRoomForMob(mob: MobEntity): RoomEntity {
        return mobRooms.find { it.mob == mob }!!.room
    }

    fun getMobsForRoom(room: RoomEntity): List<MobEntity> {
        return mobRooms.filter { it.room == room }.map { it.mob }
    }

    fun respawnMobToStartRoom(mob: MobEntity) {
        putMobInRoom(mob, rooms[0])
    }

    fun moveMob(mob: MobEntity, room: RoomEntity) {
        putMobInRoom(mob, room)
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
