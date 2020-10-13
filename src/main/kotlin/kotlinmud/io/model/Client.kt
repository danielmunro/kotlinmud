package kotlinmud.io.model

import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.ClosedChannelException
import java.nio.channels.SocketChannel
import kotlinmud.mob.dao.MobDAO
import kotlinmud.player.dao.PlayerDAO

class Client(val socket: SocketChannel) {
    val buffers: MutableList<String> = mutableListOf()
    var mob: MobDAO? = null
    var player: PlayerDAO? = null
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
        } catch (e: IOException) {
            connected = false
        }
    }

    private fun addPrompt(message: String): String {
        return "$message\n---> "
    }
}
