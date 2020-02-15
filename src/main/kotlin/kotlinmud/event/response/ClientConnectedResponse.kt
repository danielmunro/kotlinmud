package kotlinmud.event.response

import kotlinmud.event.EventResponse

class ClientConnectedResponse<T>(override val subject: T): EventResponse<T>