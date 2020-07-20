package kotlinmud.app

import java.net.ServerSocket
import kotlinmud.action.helper.createActionContextBuilder
import kotlinmud.action.helper.createActionsList
import kotlinmud.action.service.ActionService
import kotlinmud.action.service.ContextBuilderService
import kotlinmud.event.observer.impl.ChangeWeatherObserver
import kotlinmud.event.observer.impl.ClientConnectedObserver
import kotlinmud.event.observer.impl.DecrementAffectTimeoutTickObserver
import kotlinmud.event.observer.impl.DecrementDelayObserver
import kotlinmud.event.observer.impl.DecrementItemDecayTimerObserver
import kotlinmud.event.observer.impl.GrantExperienceOnKillObserver
import kotlinmud.event.observer.impl.GuardAttacksAggroMobsObserver
import kotlinmud.event.observer.impl.IncreaseThirstAndHungerObserver
import kotlinmud.event.observer.impl.LogPlayerInObserver
import kotlinmud.event.observer.impl.LogTickObserver
import kotlinmud.event.observer.impl.MoveMobsOnTickObserver
import kotlinmud.event.observer.impl.ProceedFightsPulseObserver
import kotlinmud.event.observer.impl.PruneDeadMobsPulseObserver
import kotlinmud.event.observer.impl.RegenMobsObserver
import kotlinmud.event.observer.impl.RemoveMobOnClientDisconnectObserver
import kotlinmud.event.observer.impl.SaveTimeObserver
import kotlinmud.event.observer.impl.SaveVersionsObserver
import kotlinmud.event.observer.impl.ScavengerCollectsItemsObserver
import kotlinmud.event.observer.impl.SendMessageToRoomObserver
import kotlinmud.event.observer.impl.SocialDistributorObserver
import kotlinmud.event.observer.impl.TransferGoldOnKillObserver
import kotlinmud.event.observer.impl.WimpyObserver
import kotlinmud.event.observer.type.Observers
import kotlinmud.event.service.EventService
import kotlinmud.fs.service.PersistenceService
import kotlinmud.io.service.ClientService
import kotlinmud.io.service.ServerService
import kotlinmud.item.helper.createRecipeList
import kotlinmud.item.service.ItemService
import kotlinmud.mob.service.MobService
import kotlinmud.mob.skill.helper.createSkillList
import kotlinmud.player.service.EmailService
import kotlinmud.player.service.PlayerService
import kotlinmud.service.FixtureService
import kotlinmud.service.TimeService
import kotlinmud.service.WeatherService
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
        bind<PersistenceService>() with singleton {
            PersistenceService()
        }
        bind<ActionService>() with singleton {
            ActionService(
                instance<MobService>(),
                instance<ContextBuilderService>(),
                createActionContextBuilder(
                    instance<MobService>(),
                    instance<PlayerService>(),
                    instance<ItemService>(),
                    instance<EventService>(),
                    instance<WeatherService>(),
                    instance<ServerService>()
                ),
                createActionsList()
            )
        }
        bind<MobService>() with singleton {
            MobService(
                instance<ItemService>(),
                instance<EventService>()
            )
        }
        bind<ContextBuilderService>() with singleton {
            ContextBuilderService(
                instance<ItemService>(),
                instance<MobService>(),
                instance<PlayerService>(),
                createSkillList(),
                createRecipeList()
            )
        }
        bind<Observers>() with singleton {
            listOf(
                ClientConnectedObserver(
                    instance<PlayerService>(),
                    instance<MobService>(),
                    instance<ActionService>()
                ),
                SendMessageToRoomObserver(
                    instance<ServerService>(),
                    instance<MobService>()
                ),
                RemoveMobOnClientDisconnectObserver(instance<MobService>()),
                LogPlayerInObserver(instance<MobService>()),
                ProceedFightsPulseObserver(instance<MobService>()),
                DecrementAffectTimeoutTickObserver(instance<MobService>()),
                DecrementDelayObserver(instance<ClientService>()),
                DecrementItemDecayTimerObserver(instance<ItemService>()),
                SaveTimeObserver(instance<TimeService>(), instance<PersistenceService>()),
                LogTickObserver(instance<MobService>(), instance<ServerService>()),
                PruneDeadMobsPulseObserver(instance<MobService>()),
                SocialDistributorObserver(instance<ServerService>(), instance<MobService>()),
                ChangeWeatherObserver(instance<WeatherService>()),
                SaveVersionsObserver(instance<PersistenceService>()),
                WimpyObserver(instance<MobService>()),
                GrantExperienceOnKillObserver(instance<PlayerService>(), instance<ServerService>()),
                TransferGoldOnKillObserver(),
                IncreaseThirstAndHungerObserver(instance<ServerService>()),
                RegenMobsObserver(instance<MobService>()),
                MoveMobsOnTickObserver(instance<MobService>(), instance<ItemService>(), instance<EventService>()),
                ScavengerCollectsItemsObserver(instance<MobService>(), instance<ItemService>(), instance<EventService>()),
                GuardAttacksAggroMobsObserver(instance<MobService>())
            )
        }
    }
}
