package kotlinmud.event.event

import java.net.Socket

data class ClientConnectedEvent(val socket: Socket)
