package kotlinmud.io

import kotlinmud.mob.Disposition
import kotlinmud.mob.MobEntity
import kotlinmud.room.RoomEntity

class Request(
    val mob: MobEntity,
    val input: String,
    val room: RoomEntity
) {
    val args: List<String> = input.toLowerCase().split(' ')

    fun getCommand(): String {
        return args[0]
    }

    fun getDisposition(): Disposition {
        return Disposition.valueOf(mob.disposition.toUpperCase())
    }
}
