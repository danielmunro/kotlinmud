package kotlinmud.event

data class Event<T>(val eventType: EventType, val subject: T)
