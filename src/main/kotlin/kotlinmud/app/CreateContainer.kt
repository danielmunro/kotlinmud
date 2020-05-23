package kotlinmud.app

import java.io.EOFException
import java.io.File
import java.net.ServerSocket
import kotlinmud.action.ActionService
import kotlinmud.action.createActionsList
import kotlinmud.event.EventService
import kotlinmud.event.observer.Observers
import kotlinmud.event.observer.createObservers
import kotlinmud.fs.MOB_CARD_FILE
import kotlinmud.fs.PLAYER_MOBS_FILE
import kotlinmud.fs.loadVersionState
import kotlinmud.fs.loader.Tokenizer
import kotlinmud.fs.loader.area.loader.MobLoader
import kotlinmud.fs.saver.WorldSaver
import kotlinmud.io.ClientService
import kotlinmud.io.NIOServer
import kotlinmud.item.ItemService
import kotlinmud.mob.Mob
import kotlinmud.mob.MobService
import kotlinmud.player.PlayerService
import kotlinmud.player.loader.MobCardLoader
import kotlinmud.player.loader.PlayerLoader
import kotlinmud.player.model.MobCard
import kotlinmud.service.EmailService
import kotlinmud.service.FixtureService
import kotlinmud.service.PersistenceService
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
        bind<ClientService>() with singleton { ClientService() }
        bind<NIOServer>() with singleton {
            NIOServer(
                instance< ClientService>(),
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
                run {
                    val file = File(MOB_CARD_FILE)
                    val tokenizer = Tokenizer(if (file.exists()) file.readText() else "")
                    val mobCardLoader = MobCardLoader(tokenizer)
                    val mobCards = mutableListOf<MobCard>()
                    while (true) {
                        try {
                            mobCards.add(mobCardLoader.load())
                        } catch (e: EOFException) {
                            break
                        }
                    }
                    return@run mobCards
                }
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
            World(persistenceService.loadAreas(isTest))
        }
        bind<WorldSaver>() with singleton {
            WorldSaver(instance<World>())
        }
        bind<ActionService>() with singleton {
            ActionService(
                instance<MobService>(),
                instance<ItemService>(),
                instance<EventService>(),
                instance<WeatherService>(),
                instance<NIOServer>(),
                createActionsList(instance<WorldSaver>())
            )
        }
        bind<MobService>() with singleton {
            MobService(
                instance<ItemService>(),
                instance<EventService>(),
                instance<World>(),
                run {
                    val file = File(PLAYER_MOBS_FILE)
                    val tokenizer = Tokenizer(if (file.exists()) file.readText() else "")
                    val mobLoader = MobLoader(tokenizer)
                    val mobs = mutableListOf<Mob>()
                    while (true) {
                        try {
                            mobs.add(mobLoader.load().build())
                        } catch (e: EOFException) {
                            break
                        }
                    }
                    return@run mobs
                }
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
                instance<ClientService>(),
                instance<PersistenceService>(),
                instance<World>()
            )
        }
    }
}
