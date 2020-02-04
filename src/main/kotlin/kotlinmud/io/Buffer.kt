package kotlinmud.io

import kotlinmud.action.Command
import kotlinmud.mob.Mob

class Buffer(
    private val mob: Mob,
    private val input: String,
    private val args: List<String> = input.split(' ')) {

    fun getCommand(): Command {
        return Command.valueOf(args[0])
    }
}
