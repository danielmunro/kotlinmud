package kotlinmud.event.observer

import kotlinmud.MobService
import kotlinmud.event.Event
import kotlinmud.event.MobMoveEvent

fun createObservers(mobService: MobService): Array<Observer> {
    return arrayOf(
        MobMoveObserver(mobService)
    )
}