package kotlinmud.app

import kotlinmud.event.observer.type.ObserverList
import kotlinmud.event.service.EventService
import kotlinmud.io.service.ServerService
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.service.PlayerService
import kotlinmud.room.type.Area
import kotlinmud.world.createWorld
import kotlinmud.world.service.AreaBuilderService
import org.kodein.di.direct
import org.kodein.di.erased.factory
import org.kodein.di.erased.instance

fun createApp(port: Int): App {
    val container = createContainer(port)
    val areaBuilderServiceFactory = container.direct.factory<Area, AreaBuilderService>()
    createWorld(areaBuilderServiceFactory)
    val eventService by container.instance<EventService>()
    val server by container.instance<ServerService>()
    val observers by container.instance<ObserverList>()
    eventService.observers = observers
    val playerService by container.instance<PlayerService>()
    val authStepService by container.instance<AuthStepService>()
    playerService.setAuthStepService(authStepService)

    return App(eventService, server)
}
