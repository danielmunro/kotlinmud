package kotlinmud.io.model

import java.nio.ByteBuffer
import java.nio.channels.ClosedChannelException
import java.nio.channels.SocketChannel
import kotlinmud.mob.model.Mob
import kotlinmud.player.model.Player

class Client(val socket: SocketChannel) {
    val buffers: MutableList<String> = mutableListOf()
    var mob: Mob? = null
    var player: Player? = null
    var connected = true
    var delay = 0

    fun addInput(input: String) {
        buffers.add(input)
    }

    fun writePrompt(message: String) {
        write(addPrompt(message))
    }

    fun write(message: String) {
        val buffer = ByteBuffer.allocate(1024)
        buffer.put(message.toByteArray())
        buffer.flip()
        try {
            socket.write(buffer)
        } catch (e: ClosedChannelException) {
            connected = false
        }
    }

    private fun addPrompt(message: String): String {
        return "$message\n---> "
    }
}
