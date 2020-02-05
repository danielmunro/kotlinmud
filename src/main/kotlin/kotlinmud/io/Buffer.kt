package kotlinmud.io

import kotlinmud.action.Command

class Buffer(
    private val client: ClientHandler,
    private val input: String,
    private val args: List<String> = input.split(' ')) {

    fun getCommand(): String {
        return args[0]
    }
}
