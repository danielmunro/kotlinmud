package kotlinmud.event.observer.impl.gameLoop

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.time.service.TimeService

class TimeServiceLoopObserver(private val timeService: TimeService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        timeService.loop()
    }
}
