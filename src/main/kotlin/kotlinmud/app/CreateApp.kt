package kotlinmud.app

import kotlinmud.event.observer.Observers
import kotlinmud.io.NIOServer
import kotlinmud.fs.saver.WorldSaver
import kotlinmud.service.EventService
import kotlinmud.service.ItemService
import kotlinmud.service.MobService
import kotlinmud.service.RespawnService
import kotlinmud.world.World
import org.kodein.di.erased.instance

fun createApp(port: Int): App {
    val container = createContainer(port)
    val mobService: MobService by container.instance()
    val eventService: EventService by container.instance()
    val server: NIOServer by container.instance()
    val respawnService: RespawnService by container.instance()
    val itemService: ItemService by container.instance()
    val observers: Observers by container.instance()
    eventService.observers = observers
    respawnService.respawn()
    val world: World by container.instance()
    WorldSaver(world).save()
    return App(eventService, mobService, itemService, server)
}
