package kotlinmud.event.observer.impl.round

import kotlinmud.event.impl.Event
import kotlinmud.mob.fight.Round
import kotlinmud.mob.service.MobService

fun wimpyEvent(mobService: MobService, event: Event<*>) {
    with(event.subject as Round) {
        this.getParticipants().forEach {
            if (this.isActive() && it.isWimpyMode()) {
                mobService.flee(it)
            }
        }
    }
}
