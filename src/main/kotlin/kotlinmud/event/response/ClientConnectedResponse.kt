package kotlinmud.event.response

import kotlinmud.event.EventResponse
import kotlinmud.mob.MobEntity

class ClientConnectedResponse<T>(override val subject: T, val mob: MobEntity) : EventResponse<T>
