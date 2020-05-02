package kotlinmud.app

import com.commit451.mailgun.Mailgun
import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import java.net.ServerSocket
import kotlinmud.action.createActionsList
import kotlinmud.event.observer.Observers
import kotlinmud.event.observer.createObservers
import kotlinmud.fs.getAreaResourcesFromFS
import kotlinmud.fs.loadVersionState
import kotlinmud.fs.saver.WorldSaver
import kotlinmud.io.NIOServer
import kotlinmud.service.ActionService
import kotlinmud.service.EmailService
import kotlinmud.service.EventService
import kotlinmud.service.FixtureService
import kotlinmud.service.ItemService
import kotlinmud.service.MobService
import kotlinmud.service.PersistenceService
import kotlinmud.service.PlayerService
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
        bind<PersistenceService>() with singleton {
            val versions = loadVersionState(isTest)
            PersistenceService(versions[0], versions[1])
        }
        bind<FixtureService>() with singleton { FixtureService() }
        bind<EventService>() with singleton { EventService() }
        bind<ItemService>() with singleton { ItemService() }
        bind<WeatherService>() with singleton { WeatherService() }
        bind<EmailService>() with singleton {
            val dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load()
            val domain = dotenv["MAILGUN_DOMAIN"] ?: ""
            val apiKey = dotenv["MAILGUN_API_KEY"] ?: ""
            EmailService(Mailgun.Builder(domain, apiKey).build())
        }
        bind<PlayerService>() with singleton { PlayerService(instance<EmailService>()) }
        bind<TimeService>() with singleton {
            val persistenceService = instance<PersistenceService>()
            TimeService(
                instance<EventService>(),
                if (isTest) 0 else persistenceService.loadTimeFile()
            )
        }
        bind<World>() with singleton {
            val persistenceService = instance<PersistenceService>()
            World(getAreaResourcesFromFS(persistenceService, isTest))
        }
        bind<WorldSaver>() with singleton {
            WorldSaver(instance<World>())
        }
        bind<ActionService>() with singleton {
            ActionService(
                instance<MobService>(),
                instance<ItemService>(),
                instance<EventService>(),
                instance<NIOServer>(),
                createActionsList(instance<WorldSaver>())
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
                instance<NIOServer>(),
                instance<MobService>(),
                instance<EventService>(),
                instance<RespawnService>(),
                instance<WeatherService>(),
                instance<ItemService>(),
                instance<TimeService>(),
                instance<ActionService>(),
                instance<PlayerService>(),
                instance<PersistenceService>(),
                instance<World>()
            )
        }
    }
}
