package kotlinmud.event.observer.impl

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.KillEvent
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType

class TransferGoldOnKillObserver : Observer {
    override val eventType: EventType = EventType.KILL

    override fun <T> processEvent(event: Event<T>) {
        val killEvent = event.subject as KillEvent
        val victor = killEvent.victor
        val vanquished = killEvent.vanquished

        victor.gold += vanquished.gold
        vanquished.gold = 0
    }
}
