package kotlinmud.io.model

import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.model.Mob
import kotlinmud.mob.type.Disposition
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.model.Room

data class Request(
    val mob: MobDAO,
    val input: String,
    val room: RoomDAO
) {
    val args: List<String> = input.toLowerCase().split(' ')

    fun getCommand(): String {
        return args[0]
    }

    fun getSubject(): String {
        return if (args.size > 1) args[1] else ""
    }

    fun getDisposition(): Disposition {
        return mob.disposition
    }
}
