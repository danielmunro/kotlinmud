package kotlinmud.io

import kotlinmud.MobService
import kotlinmud.action.Command
import kotlinmud.mob.MobEntity
import java.net.Socket
import java.nio.charset.Charset
import java.util.Scanner
import java.io.OutputStream

class ClientHandler(private val mobService: MobService, private val client: Socket, val mob: MobEntity) {
    private val reader: Scanner = Scanner(client.getInputStream())
    private val writer: OutputStream = client.getOutputStream()
    private var running: Boolean = false
    val request: MutableList<Request> = arrayListOf()

    fun run() {
        running = true
        while (running) {
            try {
                val text = reader.nextLine().toLowerCase()
                if (text == Command.EXIT.toString()) {
                    shutdown()
                    continue
                }
                val room = mobService.getRoomForMob(mob)
                request.add(Request(this.mob, text, room))
            } catch (ex: Exception) {
                shutdown()
            }

        }
    }

    fun isRunning(): Boolean {
        return running
    }

    fun write(message: String) {
        writer.write((message).toByteArray(Charset.defaultCharset()))
    }

    fun shiftBuffer(): Request {
        return request.removeAt(0)
    }

    private fun shutdown() {
        running = false
        client.close()
        println("${client.inetAddress.hostAddress} closed the connection")
    }

}
