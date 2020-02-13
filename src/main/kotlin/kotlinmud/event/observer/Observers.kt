package kotlinmud.event.observer

import kotlinmud.service.MobService

fun createObservers(mobService: MobService): Array<Observer> {
    return arrayOf(
        MobMoveObserver(mobService)
    )
}
