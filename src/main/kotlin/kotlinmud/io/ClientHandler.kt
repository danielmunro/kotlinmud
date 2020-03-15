package kotlinmud.io

import java.io.OutputStream
import java.net.Socket
import java.nio.charset.Charset
import java.util.Scanner
import kotlinmud.action.Command
import kotlinmud.event.EventResponse
import kotlinmud.event.createInputReceivedEvent
import kotlinmud.mob.Mob
import kotlinmud.room.Room
import kotlinmud.service.EventService

class ClientHandler(private val eventService: EventService, private val client: Socket, val mob: Mob) {
    private val reader: Scanner = Scanner(client.getInputStream())
    private val writer: OutputStream = client.getOutputStream()
    private var running: Boolean = false
    private val requests: MutableList<Request> = mutableListOf()

    fun run() {
        println("start run function")
        running = true
        while (running) {
            try {
                println("waiting for input from ${client.inetAddress}")
                val text = reader.nextLine().toLowerCase()
                println("client handler reader nextLine: \"$text\"")
                if (text == Command.EXIT.toString()) {
                    shutdown()
                    continue
                }
                val eventResponse: EventResponse<Room> = eventService.publish(
                    createInputReceivedEvent(this))
                requests.add(Request(this.mob, text, eventResponse.subject))
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
        println("writing to ${client.inetAddress}: $message")
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
