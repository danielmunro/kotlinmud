package kotlinmud.app

import kotlinmud.event.observer.type.ObserverList
import kotlinmud.event.service.EventService
import kotlinmud.persistence.service.StartupService
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.service.PlayerService
import org.kodein.di.erased.instance

fun createApp(port: Int): App {
    val container = createContainer(port)
    val app by container.instance<App>()
    val eventService by container.instance<EventService>()
    val observers by container.instance<ObserverList>()
    eventService.observers = observers
    val playerService by container.instance<PlayerService>()
    val authStepService by container.instance<AuthStepService>()
    playerService.setAuthStepService(authStepService)
    val startupService by container.instance<StartupService>()
    startupService.hydrateWorld()

    return app
}
