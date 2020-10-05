package kotlinmud.app

import kotlinmud.action.service.ActionService
import kotlinmud.event.observer.type.ObserverV2
import kotlinmud.event.service.EventService
import kotlinmud.io.service.ServerService
import kotlinmud.mob.service.MobService
import kotlinmud.player.service.PlayerService
import kotlinmud.time.service.TimeService
import org.kodein.di.erased.instance

fun createApp(port: Int): App {
    val container = createContainer(port)
    val eventService by container.instance<EventService>()
    val server by container.instance<ServerService>()
    val timeService by container.instance<TimeService>()
    val observers by container.instance<ObserverV2>()
    eventService.observersV2 = observers
    val actionService by container.instance<ActionService>()
    val playerService by container.instance<PlayerService>()
    val mobService by container.instance<MobService>()

    return App(eventService, timeService, server, actionService, playerService, mobService)
}
