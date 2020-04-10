package kotlinmud.event.event

import kotlinmud.io.NIOClient

data class ClientConnectedEvent(val client: NIOClient)
