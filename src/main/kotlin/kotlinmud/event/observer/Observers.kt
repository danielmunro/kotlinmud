package kotlinmud.event.observer

import kotlinmud.service.MobService

fun createObservers(mobService: MobService): List<Observer> {
    return listOf(
        MobMoveObserver(mobService),
        ClientConnectedObserver())
}
