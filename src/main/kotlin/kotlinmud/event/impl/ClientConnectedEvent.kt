package kotlinmud.event.impl

import kotlinmud.io.model.Client

data class ClientConnectedEvent(val client: Client)
