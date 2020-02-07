package kotlinmud

import kotlinmud.mob.Mob
import kotlinmud.mob.MobRoom
import kotlinmud.room.Room

class MobService {
    private val mobs: Array<Mob> = arrayOf()
    private val mobRooms: MutableList<MobRoom> = mutableListOf()

    fun getRoomForMob(mob: Mob): Room {
        return mobRooms.find { it.mob == mob }!!.room
    }

    fun addMobToRoom(mob: Mob, room: Room) {
        mobRooms.add(MobRoom(mob, room))
    }

    fun moveMob(mob: Mob, room: Room) {
        mobRooms.find { it.mob == mob }.also { it?.room = room }
    }
}