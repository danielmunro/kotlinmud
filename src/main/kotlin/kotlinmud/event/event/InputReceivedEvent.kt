package kotlinmud.event.event

import kotlinmud.io.NIOClient

data class InputReceivedEvent(val client: NIOClient, val input: String)
