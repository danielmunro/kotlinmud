package kotlinmud.io

import java.net.ServerSocket
import java.net.Socket
import java.util.stream.Collectors
import kotlin.streams.toList
import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.event.Pulse
import kotlinmud.event.Tick
import kotlinmud.event.createClientConnectedEvent
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
        GlobalScope.launch { timer() }
    }

    fun getClientsWithBuffers(): List<ClientHandler> {
        return clients.stream()
            .filter { it.hasRequests() }
            .toList()
    }

    fun getClientsFromMobs(mobs: List<Mob>): List<ClientHandler> {
        return mobs.mapNotNull { mob ->
            clients.find { it.mob == mob }
        }
    }

    fun getClientForMob(mob: Mob): ClientHandler? {
        return clients.find { it.mob == mob }
    }

    fun getClients(): List<ClientHandler> {
        return clients.toList()
    }

    fun getPort(): Int {
        return server.localPort
    }

    fun pruneClients() {
        val toPrune = clients.stream().filter { !it.isRunning() }.collect(Collectors.toList())
        toPrune.forEach {
            eventService.publish<ClientHandler, ClientHandler>(Event(EventType.CLIENT_DISCONNECTED, it))
        }
        clients.removeAll(toPrune)
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
