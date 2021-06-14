package kotlinmud.io.service

import kotlinmud.event.factory.createClientConnectedEvent
import kotlinmud.event.factory.createClientDisconnectedEvent
import kotlinmud.event.service.EventService
import kotlinmud.helper.logger
import kotlinmud.io.model.Client
import kotlinmud.io.type.Clients
import kotlinmud.mob.model.Mob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.withContext
import okhttp3.internal.closeQuietly
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.stream.Collectors

const val SELECT_TIMEOUT_MS: Long = 1
const val READ_BUFFER_SIZE_IN_BYTES = 1024

class ServerService(
    private val clientService: ClientService,
    private val eventService: EventService,
    val port: Int = 0,
) {
    companion object {
        fun socketChannelFromKey(key: SelectionKey): SocketChannel {
            return key.channel() as SocketChannel
        }
    }

    private val selector: Selector = Selector.open()
    private val clients: Clients = mutableListOf()
    private val socket = ServerSocketChannel.open()
    private val logger = logger(this)

    init {
        val serverSocket = socket.socket()
        serverSocket.bind(InetSocketAddress(port))
        socket.configureBlocking(false)
        val ops = socket.validOps()
        socket.register(selector, ops, null)
    }

    suspend fun removeDisconnectedClients() {
        val disconnected = clients.filter { !it.connected }
        clients.removeAll(disconnected)
        disconnected.forEach {
            eventService.publish(createClientDisconnectedEvent(it))
        }
    }

    suspend fun readIntoBuffers() {
        withContext(Dispatchers.IO) { selector.select(SELECT_TIMEOUT_MS) }
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

    fun getClients(): Clients {
        return clients
    }

    fun getLoggedInClients(): List<Client> {
        return clients.filter { it.mob != null }
    }

    fun getClientsWithBuffers(): Clients {
        return clients.stream()
            .filter { it.buffers.isNotEmpty() }
            .collect(Collectors.toList())
    }

    fun getClientForMob(mob: Mob): Client? {
        return clients.find { it.mob == mob }
    }

    fun getClientsFromMobs(mobs: List<Mob>): Clients {
        return mobs.mapNotNull { mob ->
            clients.find { it.mob == mob }
        }.toMutableList()
    }

    fun isConnected(): Boolean {
        return socket.isOpen
    }

    private suspend fun handleAccept(newSocket: ServerSocketChannel) {
        val socket = configureAndAcceptSocket(newSocket)
        val client = Client(socket)
        eventService.publish(createClientConnectedEvent(client))
        clients.add(client)
        clientService.addClient(client)
        logger.info("connection accepted :: {}", socket.remoteAddress)
    }

    private fun configureAndAcceptSocket(mySocket: ServerSocketChannel): SocketChannel {
        val socket = mySocket.accept()
        socket.configureBlocking(false)
        socket.register(selector, SelectionKey.OP_READ)
        return socket
    }

    private fun handleRead(key: SelectionKey) {
        val socket = socketChannelFromKey(key)
        try {
            readSocketIntoClient(socket)
        } catch (e: IOException) {
            logger.debug("socket io exception, closing :: {}", socket.remoteAddress)
            socket.closeQuietly()
        }
    }

    private fun readSocketIntoClient(socket: SocketChannel) {
        readSocket(socket).let {
            getClientBySocket(socket).addInput(it)
            checkSocketForQuit(socket, it)
        }
    }

    private fun checkSocketForQuit(socket: SocketChannel, data: String) {
        if (data.equals("quit", ignoreCase = true)) {
            logger.info("connection closed :: ${socket.remoteAddress}")
            socket.close()
        }
    }

    private fun readSocket(socket: SocketChannel): String {
        val buffer = ByteBuffer.allocate(READ_BUFFER_SIZE_IN_BYTES)
        socket.read(buffer)
        return String(buffer.array()).trim { it <= ' ' }
    }

    private fun getClientBySocket(socket: SocketChannel): Client {
        return clients.find { it.socket == socket }!!
    }
}
