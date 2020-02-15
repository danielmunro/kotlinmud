package kotlinmud.io

import kotlinmud.attributes.AttributesEntity
import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.event.event.ClientConnectedEvent
import kotlinmud.event.response.ClientConnectedResponse
import kotlinmud.mob.MobEntity
import java.net.ServerSocket
import java.net.Socket
import kotlinmud.service.EventService
import kotlinmud.service.MobService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Server(
    private val eventService: EventService,
    private val mobService: MobService,
    private val server: ServerSocket) {
    private var clients: MutableList<ClientHandler> = arrayListOf()

    fun start() {
        GlobalScope.launch { listenForClients() }
        GlobalScope.launch { pruneClients() }
    }

    fun getClientsWithBuffers(): Array<ClientHandler> {
        return clients.filter { it.request.size > 0 }.toTypedArray()
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
        val response: EventResponse<MobEntity> = eventService.publish(Event(EventType.CLIENT_CONNECTED, ClientConnectedEvent(socket)))
        mobService.respawnMobToStartRoom(response.subject)
        val handler = ClientHandler(mobService, socket, response.subject)
        clients.add(handler)
        GlobalScope.launch { handler.run() }
    }
}
