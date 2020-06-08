package kotlinmud.event.event

import kotlinmud.io.model.Client

data class ClientConnectedEvent(val client: Client)
