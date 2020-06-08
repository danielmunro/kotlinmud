package kotlinmud.event.event

import kotlinmud.io.model.Client

data class InputReceivedEvent(val client: Client, val input: String)
