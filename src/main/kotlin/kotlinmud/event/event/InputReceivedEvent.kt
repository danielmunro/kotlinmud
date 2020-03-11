package kotlinmud.event.event

import kotlinmud.io.ClientHandler

data class InputReceivedEvent(val client: ClientHandler)
