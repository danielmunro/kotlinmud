package kotlinmud.app

import kotlinmud.action.ActionService
import kotlinmud.event.EventService
import kotlinmud.event.observer.Observers
import kotlinmud.io.NIOServer
import kotlinmud.mob.service.MobService
import kotlinmud.player.service.PlayerService
import kotlinmud.service.RespawnService
import kotlinmud.service.TimeService
import org.kodein.di.erased.instance

fun createApp(port: Int): App {
    val container = createContainer(port)
    val mobService by container.instance<MobService>()
    val eventService by container.instance<EventService>()
    val server by container.instance<NIOServer>()
    val respawnService by container.instance<RespawnService>()
    val timeService by container.instance<TimeService>()
    val observers by container.instance<Observers>()
    val actionService by container.instance<ActionService>()
    eventService.observers = observers
    respawnService.respawn()
    val playerService by container.instance<PlayerService>()

    return App(eventService, mobService, timeService, server, actionService, playerService)
}
