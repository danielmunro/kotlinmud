package kotlinmud.event.observer

fun createObservers(): Array<Observer> {
    return arrayOf(
        MobMoveObserver()
    )
}