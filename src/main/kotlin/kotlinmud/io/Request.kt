package kotlinmud.io

import kotlinmud.mob.Disposition
import kotlinmud.mob.Mob
import kotlinmud.room.Room

class Request(
    private val client: ClientHandler,
    private val input: String,
    val room: Room) {
    val mob: Mob = client.mob
    val args: List<String> = input.split(' ')

    fun getCommand(): String {
        return args[0]
    }

    fun getDisposition(): Disposition {
        return mob.disposition
    }
}
