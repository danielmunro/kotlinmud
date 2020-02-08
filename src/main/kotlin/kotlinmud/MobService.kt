package kotlinmud

import kotlinmud.mob.Mob
import kotlinmud.mob.MobRoom
import kotlinmud.room.Room

class MobService(private val rooms: List<Room>) {
    private val mobs: List<Mob> = listOf()
    private val mobRooms: MutableList<MobRoom> = mutableListOf()

    fun getRoomForMob(mob: Mob): Room {
        return mobRooms.find { it.mob == mob }!!.room
    }

    fun respawnMobToStartRoom(mob: Mob) {
        putMobInRoom(mob, rooms[0])
    }

    fun moveMob(mob: Mob, room: Room) {
        putMobInRoom(mob, room)
    }

    private fun putMobInRoom(mob: Mob, room: Room) {
        val r = rooms.find{ it.uuid == room.uuid }
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