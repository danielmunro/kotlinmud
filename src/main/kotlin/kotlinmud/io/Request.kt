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
        return args[1]
    }

    fun getDisposition(): Disposition {
        return mob.disposition
    }
}
