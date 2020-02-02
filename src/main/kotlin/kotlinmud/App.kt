package main.kotlin.kotlinmud

import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

class App(private val server: ServerSocket) {
    var clients: MutableList<ClientHandler> = arrayListOf()

    fun processClientBuffers() {
        while (true) {
            clients.forEach { println(it.buffer) }
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
