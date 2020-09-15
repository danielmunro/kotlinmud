package kotlinmud.event.observer.impl.kill

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.KillEvent
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType
import kotlinmud.mob.service.MobService

class TransferGoldOnKillObserver(private val mobService: MobService) : Observer {
    override val eventType: EventType = EventType.KILL

    override fun <T> processEvent(event: Event<T>) {
        val killEvent = event.subject as KillEvent
        mobService.transferGold(killEvent.vanquished, killEvent.victor)
    }
}
