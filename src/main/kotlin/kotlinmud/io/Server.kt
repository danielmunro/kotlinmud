package kotlinmud.io

import java.net.ServerSocket
import java.net.Socket
import kotlinmud.event.*
import kotlinmud.event.event.PulseEvent
import kotlinmud.event.event.TickEvent
import kotlinmud.mob.Mob
import kotlinmud.service.EventService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val TICK_LENGTH_IN_SECONDS = 30

class Server(private val eventService: EventService, private val server: ServerSocket) {
    private var clients: MutableList<ClientHandler> = arrayListOf()
    private var pulses = 0
    private var ticks = 0

    fun start() {
        GlobalScope.launch { listenForClients() }
        GlobalScope.launch { pruneClients() }
        GlobalScope.launch { timer() }
    }

    fun getClientsWithBuffers(): List<ClientHandler> {
        return clients.filter { it.hasRequests() }
    }

    fun getClientsFromMobs(mobs: List<Mob>): List<ClientHandler> {
        return mobs.mapNotNull { mob ->
            clients.find { it.mob == mob }
        }
    }

    fun getClients(): List<ClientHandler> {
        return clients.toList()
    }

    private fun timer() {
        while (true) {
            Thread.sleep(2000)
            pulses++
            eventService.publish<PulseEvent, Pulse>(Event(EventType.PULSE, PulseEvent()))
            if (pulses * 2 > TICK_LENGTH_IN_SECONDS) {
                pulses = 0
                ticks++
                eventService.publish<TickEvent, Tick>(Event(EventType.TICK, TickEvent()))
            }
        }
    }

    private fun pruneClients() {
        clients = clients.filter { it.isRunning() }.toMutableList()
    }

    private fun listenForClients() {
        println("Server is running on port ${server.localPort}")
        while (true) {
            val socket = server.accept()
            println("Client connected: ${socket.inetAddress.hostAddress}")
            receiveSocket(socket)
        }
    }

    private fun receiveSocket(socket: Socket) {
        val response: EventResponse<Mob> =
            eventService.publish(createClientConnectedEvent(socket))
        val handler = ClientHandler(eventService, socket, response.subject)
        clients.add(handler)
        GlobalScope.launch { handler.run() }
    }
}
