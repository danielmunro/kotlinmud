package kotlinmud.app

import kotlinmud.action.service.ActionService
import kotlinmud.event.observer.Observers
import kotlinmud.event.service.EventService
import kotlinmud.io.service.ServerService
import kotlinmud.mob.service.MobService
import kotlinmud.player.service.PlayerService
import kotlinmud.service.RespawnService
import kotlinmud.service.TimeService
import org.kodein.di.erased.instance

fun createApp(port: Int): App {
    val container = createContainer(port)
    val mobService by container.instance<MobService>()
    val eventService by container.instance<EventService>()
    val server by container.instance<ServerService>()
    val respawnService by container.instance<RespawnService>()
    val timeService by container.instance<TimeService>()
    val observers by container.instance<Observers>()
    val actionService by container.instance<ActionService>()
    eventService.observers = observers
    respawnService.respawn()
    val playerService by container.instance<PlayerService>()

    return App(eventService, mobService, timeService, server, actionService, playerService)
}
