package kotlinmud

import java.net.ServerSocket
import kotlinmud.event.createSendMessageToRoomEvent
import kotlinmud.event.observer.createObservers
import kotlinmud.io.ClientHandler
import kotlinmud.io.Response
import kotlinmud.io.Server
import kotlinmud.io.Syntax
import kotlinmud.loader.AreaLoader
import kotlinmud.loader.World
import kotlinmud.mob.Mob
import kotlinmud.service.ActionService
import kotlinmud.service.EventService
import kotlinmud.service.FixtureService
import kotlinmud.service.ItemService
import kotlinmud.service.MobService
import kotlinmud.service.RespawnService
import kotlinmud.service.WeatherService
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

class App(
    private val eventService: EventService,
    private val mobService: MobService,
    private val server: Server
) {
    private val actionService: ActionService = ActionService(mobService, eventService, server)

    fun start() {
        println("starting app on port ${server.getPort()}")
        server.start()
        processClientBuffers()
    }

    private fun processClientBuffers() {
        while (true) {
            server.getClientsWithBuffers().forEach { processRequest(it) }
        }
    }

    private fun processRequest(client: ClientHandler) {
        if (client.mob.delay > 0) {
            return
        }
        val request = client.shiftBuffer()
        val response = actionService.run(request)
        eventService.publishRoomMessage(
            createSendMessageToRoomEvent(response.message, mobService.getRoomForMob(request.mob), request.mob, getTarget(response)))
    }

    private fun getTarget(response: Response): Mob? {
        return try {
            response.actionContextList.getResultBySyntax<Mob>(Syntax.MOB_IN_ROOM)
        } catch (e: Exception) {
            null
        }
    }
}

fun main() {
    val container = createContainer()
    val mobService: MobService by container.instance()
    val eventService: EventService by container.instance()
    val server: Server by container.instance()
    val respawnService: RespawnService by container.instance()
    val weatherService: WeatherService by container.instance()
    eventService.observers = createObservers(
        server,
        mobService,
        eventService,
        respawnService,
        weatherService
    )
    respawnService.respawn()
    App(eventService, mobService, server).start()
}

fun createContainer(): Kodein {
    return Kodein {
        bind<ServerSocket>() with singleton { ServerSocket(0) }
        bind<Server>() with singleton { Server(instance<EventService>(), instance<ServerSocket>()) }
        bind<FixtureService>() with singleton { FixtureService() }
        bind<EventService>() with singleton { EventService() }
        bind<ItemService>() with singleton { ItemService() }
        bind<WeatherService>() with singleton { WeatherService() }
        bind<ActionService>() with singleton {
            ActionService(instance<MobService>(), instance<EventService>(), instance<Server>())
        }
        bind<World>() with singleton {
            World(
                listOf(
                    AreaLoader("areas/midgard").load(),
                    AreaLoader("areas/midgard_castle").load(),
                    AreaLoader("areas/woods").load()
                )
            )
        }
        bind<MobService>() with singleton {
            MobService(instance<EventService>(), instance<World>())
        }
        bind<RespawnService>() with singleton {
            RespawnService(
                instance<World>(),
                instance<MobService>(),
                instance<ItemService>()
            )
        }
    }
}
