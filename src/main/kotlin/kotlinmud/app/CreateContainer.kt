package kotlinmud.app

import java.net.ServerSocket
import kotlinmud.event.observer.Observers
import kotlinmud.event.observer.createObservers
import kotlinmud.io.Server
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

fun createContainer(port: Int): Kodein {
    return Kodein {
        bind<ServerSocket>() with singleton { ServerSocket(port) }
        bind<Server>() with singleton {
            Server(
                instance<EventService>(),
                instance<ServerSocket>(),
                instance<TimeService>()
            )
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
                instance<Server>()
            )
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
                instance<Server>(),
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
