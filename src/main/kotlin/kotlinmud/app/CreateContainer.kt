package kotlinmud.app

import java.io.File
import java.net.ServerSocket
import kotlinmud.event.observer.Observers
import kotlinmud.event.observer.createObservers
import kotlinmud.io.NIOServer
import kotlinmud.loader.AreaLoader
import kotlinmud.saver.WorldSaver
import kotlinmud.service.ActionService
import kotlinmud.service.EventService
import kotlinmud.service.FixtureService
import kotlinmud.service.ItemService
import kotlinmud.service.MobService
import kotlinmud.service.RespawnService
import kotlinmud.service.TimeService
import kotlinmud.service.WeatherService
import kotlinmud.world.World
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

fun createContainer(port: Int, isTest: Boolean = false): Kodein {
    return Kodein {
        bind<ServerSocket>() with singleton { ServerSocket(port) }
        bind<NIOServer>() with singleton {
            NIOServer(instance<EventService>(), port)
        }
        bind<FixtureService>() with singleton { FixtureService() }
        bind<EventService>() with singleton { EventService() }
        bind<ItemService>() with singleton { ItemService() }
        bind<WeatherService>() with singleton { WeatherService() }
        bind<TimeService>() with singleton {
            TimeService(
                instance<EventService>()
            )
        }
        bind<ActionService>() with singleton {
            ActionService(
                instance<MobService>(),
                instance<ItemService>(),
                instance<EventService>(),
                instance<NIOServer>()
            )
        }
        bind<World>() with singleton {
            val areas = if (File("state").exists() && !isTest) {
                println("state exists, loading")
                listOf(AreaLoader("state/bootstrap_world").load())
            } else if (isTest) {
                listOf(
                    AreaLoader("test_areas/midgard").load(),
                    AreaLoader("test_areas/midgard_castle").load(),
                    AreaLoader("test_areas/woods").load()
                )
            } else {
                println("no state found, starting new")
                listOf(
                    AreaLoader("bootstrap_world/midgard").load(),
                    AreaLoader("bootstrap_world/midgard_castle").load(),
                    AreaLoader("bootstrap_world/woods").load()
                )
            }
            World(areas)
        }
        bind<MobService>() with singleton {
            MobService(
                instance<ItemService>(),
                instance<EventService>(),
                instance<World>()
            )
        }
        bind<RespawnService>() with singleton {
            RespawnService(
                instance<World>(),
                instance<MobService>(),
                instance<ItemService>()
            )
        }
        bind<Observers>() with singleton {
            createObservers(
                instance<NIOServer>(),
                instance<MobService>(),
                instance<EventService>(),
                instance<RespawnService>(),
                instance<WeatherService>(),
                instance<ItemService>(),
                WorldSaver(instance<World>())
            )
        }
    }
}
