package kotlinmud.event.response

import kotlinmud.event.EventResponse

class MobMoveResponse<T>(override val subject: T) : EventResponse<T>
