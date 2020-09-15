package kotlinmud.event.observer.impl.round

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType
import kotlinmud.mob.fight.Round
import kotlinmud.mob.service.MobService

class WimpyObserver(private val mobService: MobService) : Observer {
    override val eventType: EventType = EventType.FIGHT_ROUND

    override fun <T> processEvent(event: Event<T>) {
        with(event.subject as Round) {
            this.getParticipants().forEach {
                if (this.isActive() && it.isWimpyMode()) {
                    mobService.flee(it)
                }
            }
        }
    }
}
