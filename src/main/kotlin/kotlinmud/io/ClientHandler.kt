package kotlinmud.io

import java.io.OutputStream
import java.net.Socket
import java.nio.charset.Charset
import java.util.Scanner
import kotlinmud.action.Command
import kotlinmud.mob.MobEntity
import kotlinmud.service.MobService

class ClientHandler(private val mobService: MobService, private val client: Socket, val mob: MobEntity) {
    private val reader: Scanner = Scanner(client.getInputStream())
    private val writer: OutputStream = client.getOutputStream()
    private var running: Boolean = false
    private val requests: MutableList<Request> = mutableListOf()

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
                requests.add(Request(this.mob, text, room))
            } catch (ex: Exception) {
                shutdown()
            }
        }
    }

    fun hasRequests(): Boolean {
        return requests.size > 0
    }

    fun isRunning(): Boolean {
        return running
    }

    fun write(message: String) {
        if (message == "") {
            return
        }
        writer.write(("$message\n---> ").toByteArray(Charset.defaultCharset()))
    }

    fun shiftBuffer(): Request {
        return requests.removeAt(0)
    }

    private fun shutdown() {
        running = false
        client.close()
        println("${client.inetAddress.hostAddress} closed the connection")
    }
}
