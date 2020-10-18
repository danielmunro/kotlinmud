package kotlinmud.event.observer.impl.round

import kotlinmud.event.impl.Event
import kotlinmud.mob.fight.Round
import kotlinmud.mob.service.MobService
import kotlinx.coroutines.runBlocking

fun wimpyEvent(mobService: MobService, event: Event<*>) {
    with(event.subject as Round) {
        this.getParticipants().forEach {
            if (this.isActive() && it.isWimpyMode()) {
                runBlocking { mobService.flee(it) }
            }
        }
    }
}
