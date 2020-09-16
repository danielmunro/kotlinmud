package kotlinmud.app

import java.net.ServerSocket
import kotlinmud.action.helper.createActionContextBuilder
import kotlinmud.action.helper.createActionsList
import kotlinmud.action.service.ActionService
import kotlinmud.action.service.ContextBuilderService
import kotlinmud.biome.helper.createBiomes
import kotlinmud.db.createConnection
import kotlinmud.event.observer.impl.ClientConnectedObserver
import kotlinmud.event.observer.impl.GuardAttacksAggroMobsObserver
import kotlinmud.event.observer.impl.LogOutAllPlayersOnStartupObserver
import kotlinmud.event.observer.impl.LogPlayerInObserver
import kotlinmud.event.observer.impl.LogPlayerOutObserver
import kotlinmud.event.observer.impl.SendMessageToRoomObserver
import kotlinmud.event.observer.impl.SocialDistributorObserver
import kotlinmud.event.observer.impl.kill.GrantExperienceOnKillObserver
import kotlinmud.event.observer.impl.kill.TransferGoldOnKillObserver
import kotlinmud.event.observer.impl.pulse.ProceedFightsPulseObserver
import kotlinmud.event.observer.impl.pulse.PruneDeadMobsPulseObserver
import kotlinmud.event.observer.impl.regen.FastHealingObserver
import kotlinmud.event.observer.impl.regen.MeditationObserver
import kotlinmud.event.observer.impl.round.EnhancedDamageObserver
import kotlinmud.event.observer.impl.round.SecondAttackObserver
import kotlinmud.event.observer.impl.round.WimpyObserver
import kotlinmud.event.observer.impl.tick.ChangeWeatherObserver
import kotlinmud.event.observer.impl.tick.DecrementAffectTimeoutTickObserver
import kotlinmud.event.observer.impl.tick.DecrementDelayObserver
import kotlinmud.event.observer.impl.tick.DecrementItemDecayTimerObserver
import kotlinmud.event.observer.impl.tick.GenerateMobsObserver
import kotlinmud.event.observer.impl.tick.IncreaseThirstAndHungerObserver
import kotlinmud.event.observer.impl.tick.LogTickObserver
import kotlinmud.event.observer.impl.tick.MoveMobsOnTickObserver
import kotlinmud.event.observer.impl.tick.RegenMobsObserver
import kotlinmud.event.observer.impl.tick.ScavengerCollectsItemsObserver
import kotlinmud.event.observer.type.Observers
import kotlinmud.event.service.EventService
import kotlinmud.generator.MobGeneratorService
import kotlinmud.io.service.ClientService
import kotlinmud.io.service.ServerService
import kotlinmud.item.helper.createRecipeList
import kotlinmud.item.service.ItemService
import kotlinmud.mob.service.MobService
import kotlinmud.mob.skill.helper.createSkillList
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.factory.createEmailService
import kotlinmud.player.factory.createEmailServiceMock
import kotlinmud.player.service.EmailService
import kotlinmud.player.service.PlayerService
import kotlinmud.service.FixtureService
import kotlinmud.service.WeatherService
import kotlinmud.time.service.TimeService
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

fun createContainer(port: Int, test: Boolean = false): Kodein {
    return Kodein {
        createConnection()
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
            if (test) {
                createEmailServiceMock()
            } else {
                createEmailService()
            }
        }
        bind<PlayerService>() with singleton {
            PlayerService(
                instance<EmailService>(),
                instance<EventService>()
            )
        }
        bind<AuthStepService>() with singleton {
            val playerService = instance<PlayerService>()
            AuthStepService(playerService).also {
                playerService.setAuthStepService(it)
            }
        }
        bind<TimeService>() with singleton {
            TimeService(instance<EventService>())
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
                instance<EventService>(),
                createSkillList()
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
        bind<MobGeneratorService>() with singleton {
            MobGeneratorService(createBiomes())
        }
        bind<Observers>() with singleton {
            listOf(
                ClientConnectedObserver(
                    instance<PlayerService>()
                ),
                SendMessageToRoomObserver(
                    instance<ServerService>()
                ),
                LogPlayerInObserver(),
                LogPlayerOutObserver(),
                ProceedFightsPulseObserver(instance<MobService>()),
                DecrementAffectTimeoutTickObserver(instance<MobService>()),
                DecrementDelayObserver(instance<ClientService>()),
                DecrementItemDecayTimerObserver(instance<ItemService>()),
                LogTickObserver(instance<ServerService>()),
                PruneDeadMobsPulseObserver(instance<MobService>()),
                SocialDistributorObserver(instance<ServerService>()),
                ChangeWeatherObserver(instance<WeatherService>()),
                WimpyObserver(instance<MobService>()),
                GrantExperienceOnKillObserver(instance<ServerService>()),
                TransferGoldOnKillObserver(instance<MobService>()),
                IncreaseThirstAndHungerObserver(instance<ServerService>()),
                RegenMobsObserver(instance<MobService>()),
                MoveMobsOnTickObserver(instance<MobService>(), instance<ItemService>(), instance<EventService>()),
                ScavengerCollectsItemsObserver(instance<MobService>(), instance<ItemService>(), instance<EventService>()),
                GuardAttacksAggroMobsObserver(instance<MobService>()),
                GenerateMobsObserver(instance<MobGeneratorService>()),
                LogOutAllPlayersOnStartupObserver(instance<PlayerService>()),
                EnhancedDamageObserver(),
                SecondAttackObserver(),
                FastHealingObserver(),
                MeditationObserver()
            )
        }
    }
}
