package kotlinmud.io

import kotlinmud.mob.Disposition
import kotlinmud.mob.Mob
import kotlinmud.room.Room

class Request(
    val mob: Mob,
    val input: String,
    val room: Room) {
    val args: List<String> = input.toLowerCase().split(' ')

    fun getCommand(): String {
        return args[0]
    }

    fun getTarget(): String {
        return args[1]
    }

    fun getDisposition(): Disposition {
        return mob.disposition
    }
}
