package kotlinmud.event.observer.impl

import kotlinmud.app.App
import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer

class RebootObserver(private val app: App) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
//        notify players
        app.stop()
    }
}
