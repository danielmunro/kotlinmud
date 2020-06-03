package kotlinmud.event.event

import kotlinmud.io.model.NIOClient

data class InputReceivedEvent(val client: NIOClient, val input: String)
