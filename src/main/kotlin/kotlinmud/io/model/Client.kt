package kotlinmud.io.model

import kotlinmud.mob.model.PlayerMob
import kotlinmud.player.dao.PlayerDAO
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.ClosedChannelException
import java.nio.channels.SocketChannel

class Client(val socket: SocketChannel) {
    companion object {
        var autoIncrementor = 0
    }
    val id = autoIncrementor++
    val buffers: MutableList<String> = mutableListOf()
    var mob: PlayerMob? = null
    var player: PlayerDAO? = null
    var connected = true
    var delay = 0

    fun isDelayed(): Boolean {
        return delay > 0
    }

    fun shiftInput(): String {
        return buffers.removeAt(0)
    }

    fun isInGame(): Boolean {
        return mob != null
    }

    fun addInput(input: String) {
        buffers.add(input)
    }

    fun writePrompt(message: String) {
        write(addPrompt(message))
    }

    fun write(message: String) {
        val buffer = ByteBuffer.allocate(1024)
        println(message)
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
