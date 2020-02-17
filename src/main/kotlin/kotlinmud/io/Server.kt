package kotlinmud.io

import java.net.ServerSocket
import java.net.Socket
import kotlinmud.event.EventResponse
import kotlinmud.event.createClientConnectedEvent
import kotlinmud.mob.MobEntity
import kotlinmud.service.EventService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Server(private val eventService: EventService, private val server: ServerSocket) {
    private var clients: MutableList<ClientHandler> = arrayListOf()

    fun start() {
        GlobalScope.launch { listenForClients() }
        GlobalScope.launch { pruneClients() }
    }

    fun getClientsWithBuffers(): List<ClientHandler> {
        return clients.filter { it.hasRequests() }
    }

    fun getClientsFromMobs(mobs: List<MobEntity>): List<ClientHandler> {
        return mobs.mapNotNull { mob ->
            clients.find { it.mob == mob }
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
        val response: EventResponse<MobEntity> =
            eventService.publish(createClientConnectedEvent(socket))
        val handler = ClientHandler(eventService, socket, response.subject)
        clients.add(handler)
        GlobalScope.launch { handler.run() }
    }
}
