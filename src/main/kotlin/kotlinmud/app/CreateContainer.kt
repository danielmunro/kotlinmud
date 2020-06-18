package kotlinmud.app

import java.net.ServerSocket
import kotlinmud.action.helper.createActionContextBuilder
import kotlinmud.action.helper.createActionsList
import kotlinmud.action.service.ActionService
import kotlinmud.event.observer.Observers
import kotlinmud.event.observer.createObservers
import kotlinmud.event.service.EventService
import kotlinmud.fs.helper.loadVersionState
import kotlinmud.fs.saver.WorldSaver
import kotlinmud.fs.service.PersistenceService
import kotlinmud.io.service.ClientService
import kotlinmud.io.service.ServerService
import kotlinmud.item.service.ItemService
import kotlinmud.mob.provider.loadMobs
import kotlinmud.mob.service.MobService
import kotlinmud.player.loader.PlayerLoader
import kotlinmud.player.provider.loadMobCards
import kotlinmud.player.service.EmailService
import kotlinmud.player.service.PlayerService
import kotlinmud.service.FixtureService
import kotlinmud.service.RespawnService
import kotlinmud.service.TimeService
import kotlinmud.service.WeatherService
import kotlinmud.world.helper.mapAreasToModel
import kotlinmud.world.model.World
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

fun createContainer(port: Int, isTest: Boolean = false): Kodein {
    return Kodein {
        bind<ServerSocket>() with singleton { ServerSocket(port) }
        bind<ClientService>() with singleton { ClientService() }
        bind<ServerService>() with singleton {
            ServerService(
                instance<ClientService>(),
                instance<EventService>(),
                port
            )
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
            val dotenv = getDotenv()
            val domain = dotenv["MAILGUN_DOMAIN"] ?: ""
            val apiKey = dotenv["MAILGUN_API_KEY"] ?: ""
            EmailService(getMailgunClient(domain, apiKey))
        }
        bind<PlayerService>() with singleton {
            PlayerService(
                instance<EmailService>(),
                PlayerLoader.loadAllPlayers().toMutableList(),
                loadMobCards(),
                instance<EventService>()
            )
        }
        bind<TimeService>() with singleton {
            val persistenceService = instance<PersistenceService>()
            TimeService(
                instance<EventService>(),
                if (isTest) 0 else persistenceService.loadTimeFile()
            )
        }
        bind<World>() with singleton {
            val persistenceService = instance<PersistenceService>()
            mapAreasToModel(persistenceService.loadAreas(isTest))
        }
        bind<WorldSaver>() with singleton {
            WorldSaver(instance<World>())
        }
        bind<ActionService>() with singleton {
            ActionService(
                instance<MobService>(),
                instance<PlayerService>(),
                instance<ItemService>(),
                createActionContextBuilder(
                    instance<MobService>(),
                    instance<PlayerService>(),
                    instance<ItemService>(),
                    instance<EventService>(),
                    instance<WeatherService>(),
                    instance<ServerService>()
                ),
                createActionsList(instance<WorldSaver>())
            )
        }
        bind<MobService>() with singleton {
            val persistenceService = instance<PersistenceService>()
            MobService(
                instance<ItemService>(),
                instance<EventService>(),
                instance<World>(),
                loadMobs(persistenceService.loadSchemaToUse)
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
                instance<ServerService>(),
                instance<MobService>(),
                instance<EventService>(),
                instance<RespawnService>(),
                instance<WeatherService>(),
                instance<ItemService>(),
                instance<TimeService>(),
                instance<ActionService>(),
                instance<PlayerService>(),
                instance<ClientService>(),
                instance<PersistenceService>(),
                instance<World>()
            )
        }
    }
}
