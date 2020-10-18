package kotlinmud.event.observer.type

import kotlinmud.event.impl.Event
import kotlinmud.event.type.EventType

typealias Observer = Map<EventType, List<(Event<*>) -> Unit>>
