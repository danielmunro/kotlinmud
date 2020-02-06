package kotlinmud.io

import kotlinmud.MobService
import kotlinmud.item.Inventory
import kotlinmud.item.Item
import kotlinmud.mob.Disposition
import kotlinmud.mob.Mob
import java.net.ServerSocket
import java.net.Socket
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.LazySizedCollection
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.SizedIterable
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

    fun getClientCount(): Int {
        return clients.size
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
        val handler = ClientHandler(mobService, socket, transaction { Mob.new{
            name = "a test mob"
            description = "a test mob is here, being a test."
            disposition = Disposition.STANDING
            inventory = Inventory.new{}
        } })
        clients.add(handler)
        GlobalScope.launch { handler.run() }
    }
}