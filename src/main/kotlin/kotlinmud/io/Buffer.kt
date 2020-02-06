package kotlinmud.io

import kotlinmud.mob.Disposition
import kotlinmud.mob.Mob

class Buffer(
    private val client: ClientHandler,
    private val input: String,
    private val args: List<String> = input.split(' ')) {

    private val mob: Mob = client.mob

    fun getCommand(): String {
        return args[0]
    }

    fun getDisposition(): Disposition {
        return mob.disposition
    }
}
