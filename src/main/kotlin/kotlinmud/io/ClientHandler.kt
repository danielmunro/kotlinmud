package kotlinmud.io

import kotlinmud.action.Command
import kotlinmud.mob.Mob
import java.net.Socket
import java.nio.charset.Charset
import java.util.Scanner
import java.io.OutputStream

class ClientHandler(private val client: Socket, val mob: Mob) {
    private val reader: Scanner = Scanner(client.getInputStream())
    private val writer: OutputStream = client.getOutputStream()
    private var running: Boolean = false
    val buffer: MutableList<Buffer> = arrayListOf()

    fun run() {
        running = true
        while (running) {
            try {
                val text = reader.nextLine().toLowerCase()
                if (text == Command.EXIT.toString()) {
                    shutdown()
                    continue
                }
                buffer.add(Buffer(this, text))
            } catch (ex: Exception) {
                shutdown()
            }

        }
    }

    fun isRunning(): Boolean {
        return running
    }

    fun write(message: String) {
        writer.write((message + '\n').toByteArray(Charset.defaultCharset()))
    }

    fun shiftBuffer(): Buffer {
        return buffer.removeAt(0)
    }

    private fun shutdown() {
        running = false
        client.close()
        println("${client.inetAddress.hostAddress} closed the connection")
    }

}
