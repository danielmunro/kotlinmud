package kotlinmud.event.event

import kotlinmud.io.model.NIOClient

data class ClientConnectedEvent(val client: NIOClient)
