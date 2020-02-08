package kotlinmud.io

import kotlinmud.mob.Disposition
import kotlinmud.mob.Mob
import kotlinmud.room.Room

class Request(
    val client: ClientHandler,
    val input: String,
    val room: Room) {
    val mob: Mob = client.mob
    val args: List<String> = input.toLowerCase().split(' ')

    fun getCommand(): String {
        return args[0]
    }

    fun getDisposition(): Disposition {
        return mob.disposition
    }
}
