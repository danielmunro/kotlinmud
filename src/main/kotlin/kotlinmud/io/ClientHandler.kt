package kotlinmud.io

import java.io.OutputStream
import java.net.Socket
import java.nio.charset.Charset
import java.util.Scanner
import kotlinmud.action.Command
import kotlinmud.event.createInputReceivedEvent
import kotlinmud.event.event.InputReceivedEvent
import kotlinmud.mob.Mob
import kotlinmud.service.EventService

class ClientHandler(private val eventService: EventService, private val client: Socket, val mob: Mob) {
    private val reader: Scanner = Scanner(client.getInputStream())
    private val writer: OutputStream = client.getOutputStream()
    private var running: Boolean = false
    private val requests: MutableList<Request> = mutableListOf()

    fun run() {
        running = true
        while (running) {
            try {
                reader.nextLine().toLowerCase().let { captureInput(it) }
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

    fun addRequest(request: Request) {
        requests.add(request)
    }

    private fun captureInput(input: String) {
        println("client handler reader nextLine: \"$input\"")
        if (Command.EXIT.value == input) {
            shutdown()
            return
        }
        eventService.publish<InputReceivedEvent, InputReceivedEvent>(createInputReceivedEvent(this, input))
    }

    private fun shutdown() {
        running = false
        client.close()
        println("${client.inetAddress.hostAddress} closed the connection")
    }
}
