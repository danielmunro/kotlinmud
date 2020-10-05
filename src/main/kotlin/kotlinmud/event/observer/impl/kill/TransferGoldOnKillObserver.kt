package kotlinmud.event.observer.impl.kill

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.KillEvent
import kotlinmud.mob.service.MobService

fun transferGoldOnKillEvent(mobService: MobService, event: Event<*>) {
    val killEvent = event.subject as KillEvent
    mobService.transferGold(killEvent.vanquished, killEvent.victor)
}
