package kotlinmud.event.response

import kotlinmud.event.EventResponse

class SendMessageToRoomResponse<T>(override val subject: T) : EventResponse<T>
