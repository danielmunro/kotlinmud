package kotlinmud.app

import kotlinmud.event.observer.type.ObserverList
import kotlinmud.event.service.EventService
import kotlinmud.helper.getAllDataFiles
import kotlinmud.io.service.ServerService
import kotlinmud.item.service.ItemService
import kotlinmud.mob.service.MobService
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.service.PlayerService
import kotlinmud.room.service.RoomService
import kotlinmud.startup.service.StartupService
import org.kodein.di.erased.instance

fun createApp(port: Int): App {
    val container = createContainer(port)
    val eventService by container.instance<EventService>()
    val server by container.instance<ServerService>()
    val observers by container.instance<ObserverList>()
    eventService.observers = observers
    val playerService by container.instance<PlayerService>()
    val authStepService by container.instance<AuthStepService>()
    playerService.setAuthStepService(authStepService)
    val roomService by container.instance<RoomService>()
    val mobService by container.instance<MobService>()
    val itemService by container.instance<ItemService>()
    val data = getAllDataFiles()
    val svc = StartupService(roomService, mobService, itemService, data)
    svc.hydrateWorld()

    return App(eventService, server)
}
