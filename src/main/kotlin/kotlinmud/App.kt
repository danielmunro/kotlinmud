package kotlinmud

import kotlinmud.io.ClientHandler
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

class App(private val server: ServerSocket) {
    private var clients: MutableList<ClientHandler> = arrayListOf()
    private val actionService: ActionService = ActionService()

    fun processClientBuffers() {
        while (true) {
            clients.forEach {
                if (it.buffer.size > 0) {
                    val input = it.buffer.removeAt(0)
                    println("pop off: $input")
                }
                println(it.buffer)
            }
            clients = clients.filter { it.isRunning() }.toMutableList()
            Thread.sleep(1000)
        }
    }

    fun listenForClients() {
        println("Server is running on port ${server.localPort}")
        while (true) {
            val socket = server.accept()
            println("Client connected: ${socket.inetAddress.hostAddress}")
            receiveSocket(socket)
        }
    }

    private fun receiveSocket(socket: Socket) {
        val handler = ClientHandler(socket)
        clients.add(handler)
        thread { handler.run() }
    }
}

fun main() {
    val app = App(ServerSocket(9999))

    thread { app.processClientBuffers() }
    thread { app.listenForClients() }
}
