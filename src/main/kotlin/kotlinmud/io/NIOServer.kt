package kotlinmud.io

import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.stream.Collectors
import kotlinmud.event.EventResponse
import kotlinmud.event.createClientConnectedEvent
import kotlinmud.event.createClientDisconnectedEvent
import kotlinmud.event.event.ClientConnectedEvent
import kotlinmud.mob.Mob
import kotlinmud.service.EventService

const val SELECT_TIMEOUT_MS: Long = 1
const val READ_BUFFER_SIZE_IN_BYTES = 1024

class NIOServer(private val eventService: EventService, val port: Int = 0) {
    private val selector: Selector = Selector.open()
    private val clients: NIOClients = mutableListOf()
    private val socket = ServerSocketChannel.open()

    fun configure() {
        val serverSocket = socket.socket()
        serverSocket.bind(InetSocketAddress(port))
        socket.configureBlocking(false)
        val ops = socket.validOps()
        socket.register(selector, ops, null)
    }

    fun removeDisconnectedClients() {
        val lost = clients.stream()
            .filter { !it.connected }
            .collect(Collectors.toList())
        lost.forEach {
            eventService.publish<NIOClient, NIOClient>(createClientDisconnectedEvent(it))
        }
        clients.removeAll(lost)
    }

    fun readIntoBuffers() {
        selector.select(SELECT_TIMEOUT_MS)
        val selectedKeys: MutableSet<SelectionKey> = selector.selectedKeys()
        val i = selectedKeys.iterator()
        while (i.hasNext()) {
            val key = i.next()
            if (key.isAcceptable) {
                handleAccept(socket)
            } else if (key.isReadable) {
                handleRead(key)
            }
            i.remove()
        }
    }

    fun getClients(): NIOClients {
        return clients
    }

    fun getClientsWithBuffers(): NIOClients {
        return clients.stream().filter { it.buffers.isNotEmpty() }.collect(Collectors.toList())
    }

    fun getClientForMob(mob: Mob): NIOClient? {
        return clients.find { it.mob == mob }
    }

    fun getClientsFromMobs(mobs: List<Mob>): NIOClients {
        return mobs.mapNotNull { mob ->
            clients.find { it.mob == mob }
        }.toMutableList()
    }

    private fun handleAccept(mySocket: ServerSocketChannel) {
        val socket = mySocket.accept()
        socket.configureBlocking(false)
        socket.register(selector, SelectionKey.OP_READ)
        val client = NIOClient(socket)
        eventService.publish<ClientConnectedEvent, EventResponse<Mob>>(createClientConnectedEvent(client))
        clients.add(client)
        println("connection accepted :: ${socket.remoteAddress}")
    }

    private fun handleRead(key: SelectionKey) {
        val socket = socketChannelFromKey(key)
        readSocket(socket).let {
            getClientBySocket(socket).addInput(it)
            checkSocketForQuit(socket, it)
        }
    }

    private fun checkSocketForQuit(socket: SocketChannel, data: String) {
        if (data.equals("quit", ignoreCase = true)) {
            socket.close()
            println("connection closed :: ${socket.remoteAddress}")
        }
    }

    private fun readSocket(socket: SocketChannel): String {
        val buffer = ByteBuffer.allocate(READ_BUFFER_SIZE_IN_BYTES)
        socket.read(buffer)
        return String(buffer.array()).trim { it <= ' ' }
    }

    private fun getClientBySocket(socket: SocketChannel): NIOClient {
        return clients.find { it.socket == socket }!!
    }
}

fun socketChannelFromKey(key: SelectionKey): SocketChannel {
    return key.channel() as SocketChannel
}
