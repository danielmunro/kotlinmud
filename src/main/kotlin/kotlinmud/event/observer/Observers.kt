package kotlinmud.event.observer

import kotlinmud.MobService

fun createObservers(mobService: MobService): Array<Observer> {
    return arrayOf(
        MobMoveObserver(mobService)
    )
}