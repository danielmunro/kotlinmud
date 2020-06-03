package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.observer.Observer
import kotlinmud.item.service.ItemService

class DecrementItemDecayTimerObserver(private val itemService: ItemService) : Observer {
    override val eventType: EventType = EventType.TICK

    override fun <T> processEvent(event: Event<T>) {
        itemService.decrementDecayTimer()
    }
}
