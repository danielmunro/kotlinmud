package kotlinmud.io

import java.net.ServerSocket
import java.net.Socket
import kotlinmud.item.InventoryEntity
import kotlinmud.mob.Disposition
import kotlinmud.mob.MobEntity
import kotlinmud.service.MobService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.transactions.transaction

class Server(private val mobService: MobService, private val server: ServerSocket) {
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
        val mob = transaction {
            MobEntity.new {
                name = "a test mob"
                description = "a test mob is here, being a test."
                disposition = Disposition.STANDING.value
                inventory = InventoryEntity.new {}
            }
        }
        mobService.respawnMobToStartRoom(mob)
        val handler = ClientHandler(mobService, socket, mob)
        clients.add(handler)
        GlobalScope.launch { handler.run() }
    }
}
