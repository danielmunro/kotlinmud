package kotlinmud.io

import java.net.ServerSocket
import java.net.Socket
import kotlinmud.event.EventResponse
import kotlinmud.event.createClientConnectedEvent
import kotlinmud.mob.MobEntity
import kotlinmud.service.EventService
import kotlinmud.service.MobService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Server(
    private val eventService: EventService,
    private val mobService: MobService,
    private val server: ServerSocket
) {
    private var clients: MutableList<ClientHandler> = arrayListOf()

    fun start() {
        GlobalScope.launch { listenForClients() }
        GlobalScope.launch { pruneClients() }
    }

    fun getClientsWithBuffers(): List<ClientHandler> {
        return clients.filter { it.request.size > 0 }
    }

    fun getClientsFromMobs(mobs: List<MobEntity>): List<ClientHandler> {
        val clientsToReturn = mutableListOf<ClientHandler>()
        mobs.forEach { mob ->
            clients.find { it.mob == mob }?.let { clientsToReturn.add(it) }
        }
        return clientsToReturn
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
        val response: EventResponse<MobEntity> = eventService.publish(createClientConnectedEvent(socket))
        mobService.respawnMobToStartRoom(response.subject)
        val handler = ClientHandler(mobService, socket, response.subject)
        clients.add(handler)
        GlobalScope.launch { handler.run() }
    }
}
