package kotlinmud.event.impl

import kotlinmud.event.type.EventType

open class Event<T>(val eventType: EventType, val subject: T)
