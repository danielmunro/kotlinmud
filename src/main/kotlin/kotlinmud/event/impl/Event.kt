package kotlinmud.event.impl

import kotlinmud.event.type.EventType

data class Event<T>(val eventType: EventType, val subject: T)
