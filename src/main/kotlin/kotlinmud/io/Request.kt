package kotlinmud.io

import kotlinmud.mob.Disposition
import kotlinmud.mob.Mob
import kotlinmud.world.room.Room

data class Request(
    val mob: Mob,
    val input: String,
    val room: Room
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
