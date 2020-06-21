package kotlinmud.event.observer.impl

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType
import kotlinmud.item.service.ItemService

class DecrementItemDecayTimerObserver(private val itemService: ItemService) :
    Observer {
    override val eventType: EventType = EventType.TICK

    override fun <T> processEvent(event: Event<T>) {
        itemService.decrementDecayTimer()
    }
}
