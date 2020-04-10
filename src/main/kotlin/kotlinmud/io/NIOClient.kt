package kotlinmud.io

import java.nio.ByteBuffer
import java.nio.channels.ClosedChannelException
import java.nio.channels.SocketChannel
import kotlinmud.mob.Mob

class NIOClient(val socket: SocketChannel) {
    val buffers: MutableList<String> = mutableListOf()
    var mob: Mob? = null
    var connected = true

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
