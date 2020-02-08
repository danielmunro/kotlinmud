package kotlinmud.io

import kotlinmud.MobService
import kotlinmud.exit.Exit
import kotlinmud.item.Inventory
import kotlinmud.mob.Disposition
import kotlinmud.mob.Mob
import kotlinmud.room.Direction
import kotlinmud.room.Room
import kotlinmud.room.Rooms
import java.net.ServerSocket
import java.net.Socket
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.selectAll
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
        transaction {
            val mob = Mob.new {
                name = "a test mob"
                description = "a test mob is here, being a test."
                disposition = Disposition.STANDING
                inventory = Inventory.new {}
            }
            val room1 = Room.new {
                name = "a test room 1"
                description = "a test room is here"
                inventory = Inventory.new {}
            }
            val room2 = Room.new {
                name = "a test room 2"
                description = "a test room is here"
                inventory = Inventory.new {}
            }
            val exit1 = Exit.new {
                room = room1
                destination = room2
                direction = Direction.NORTH
            }
            val exit2 = Exit.new {
                room = room2
                destination = room1
                direction = Direction.SOUTH
            }
            val loaded = Room.all().first()
            mobService.addMobToRoom(mob, loaded)
            val handler = ClientHandler(mobService, socket, mob)
            clients.add(handler)
            GlobalScope.launch { handler.run() }
        }
    }
}
