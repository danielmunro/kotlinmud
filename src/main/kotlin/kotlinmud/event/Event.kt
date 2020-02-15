package kotlinmud.event

class Event<T>(val eventType: EventType, val subject: T)
