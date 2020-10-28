package kotlinmud.app

import java.net.ServerSocket
import kotlinmud.action.helper.createActionContextBuilder
import kotlinmud.action.helper.createActionsList
import kotlinmud.action.service.ActionService
import kotlinmud.action.service.ContextBuilderService
import kotlinmud.app.db.createConnection
import kotlinmud.biome.helper.createBiomes
import kotlinmud.event.observer.impl.GuardAttacksAggroMobsObserver
import kotlinmud.event.observer.impl.LogOutAllPlayersOnStartupObserver
import kotlinmud.event.observer.impl.SendMessageToRoomObserver
import kotlinmud.event.observer.impl.SocialDistributorObserver
import kotlinmud.event.observer.impl.client.ClientConnectedObserver
import kotlinmud.event.observer.impl.client.LogPlayerInObserver
import kotlinmud.event.observer.impl.client.LogPlayerOutObserver
import kotlinmud.event.observer.impl.gameLoop.ProcessClientBuffersObserver
import kotlinmud.event.observer.impl.gameLoop.ReadIntoClientBuffersObserver
import kotlinmud.event.observer.impl.gameLoop.RemoveDisconnectedClients
import kotlinmud.event.observer.impl.gameLoop.TimeServiceLoopObserver
import kotlinmud.event.observer.impl.kill.GrantExperienceOnKillObserver
import kotlinmud.event.observer.impl.kill.TransferGoldOnKillObserver
import kotlinmud.event.observer.impl.pulse.ProceedFightsPulseObserver
import kotlinmud.event.observer.impl.pulse.PruneDeadMobsPulseObserver
import kotlinmud.event.observer.impl.regen.FastHealingObserver
import kotlinmud.event.observer.impl.regen.MeditationObserver
import kotlinmud.event.observer.impl.round.EnhancedDamageObserver
import kotlinmud.event.observer.impl.round.SecondAttackObserver
import kotlinmud.event.observer.impl.round.ThirdAttackObserver
import kotlinmud.event.observer.impl.round.WimpyObserver
import kotlinmud.event.observer.impl.tick.ChangeWeatherObserver
import kotlinmud.event.observer.impl.tick.DecreaseThirstAndHungerObserver
import kotlinmud.event.observer.impl.tick.DecrementAffectTimeoutTickObserver
import kotlinmud.event.observer.impl.tick.DecrementDelayObserver
import kotlinmud.event.observer.impl.tick.DecrementItemDecayTimerObserver
import kotlinmud.event.observer.impl.tick.GenerateGrassObserver
import kotlinmud.event.observer.impl.tick.GenerateMobsObserver
import kotlinmud.event.observer.impl.tick.GrowResourcesObserver
import kotlinmud.event.observer.impl.tick.LogTickObserver
import kotlinmud.event.observer.impl.tick.MoveMobsOnTickObserver
import kotlinmud.event.observer.impl.tick.RegenMobsObserver
import kotlinmud.event.observer.impl.tick.ScavengerCollectsItemsObserver
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.observer.type.ObserverList
import kotlinmud.event.service.EventService
import kotlinmud.event.type.EventType
import kotlinmud.generator.service.FixtureService
import kotlinmud.generator.service.MobGeneratorService
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
import kotlinmud.resource.service.ResourceService
import kotlinmud.time.service.TimeService
import kotlinmud.weather.service.WeatherService
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.provider
import org.kodein.di.erased.singleton

fun createContainer(port: Int, test: Boolean = false): Kodein {
    return Kodein {
        createConnection()
        bind<ServerSocket>() with singleton { ServerSocket(port) }
        bind<ClientService>() with singleton { ClientService() }
        bind<ServerService>() with singleton {
            ServerService(instance(), instance(), port)
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
            PlayerService(instance(), instance())
        }
        bind<AuthStepService>() with singleton {
            val playerService = instance<PlayerService>()
            AuthStepService(playerService).also {
                playerService.setAuthStepService(it)
            }
        }
        bind<TimeService>() with singleton {
            TimeService(instance())
        }
        bind<ActionService>() with singleton {
            ActionService(
                instance(),
                instance(),
                createActionContextBuilder(
                    instance(),
                    instance(),
                    instance(),
                    instance(),
                    instance(),
                    instance()
                ),
                createActionsList()
            )
        }
        bind<MobService>() with singleton {
            MobService(
                instance(),
                instance(),
                createSkillList()
            )
        }
        bind<ContextBuilderService>() with singleton {
            ContextBuilderService(
                instance(),
                instance(),
                instance(),
                createSkillList(),
                createRecipeList()
            )
        }
        bind<MobGeneratorService>() with singleton {
            MobGeneratorService(createBiomes())
        }
        bind<ResourceService>() with singleton {
            ResourceService()
        }

        bind<Observer>(tag = "logoutAllPlayersOnStartup") with provider {
            LogOutAllPlayersOnStartupObserver(instance())
        }

        bind<Observer>(tag = "clientConnected") with provider {
            ClientConnectedObserver(instance())
        }

        bind<Observer>(tag = "sendMessageToRoom") with provider {
            SendMessageToRoomObserver(instance())
        }

        bind<Observer>(tag = "logPlayerIn") with provider {
            LogPlayerInObserver()
        }

        bind<Observer>(tag = "logPlayerOut") with provider {
            LogPlayerOutObserver()
        }

        bind<Observer>(tag = "social") with provider {
            SocialDistributorObserver(instance())
        }

        bind<Observer>(tag = "proceedFights") with provider {
            ProceedFightsPulseObserver(instance())
        }

        bind<Observer>(tag = "decrementAffectTimeout") with provider {
            DecrementAffectTimeoutTickObserver(instance())
        }

        bind<Observer>(tag = "pruneDeadMobs") with provider {
            PruneDeadMobsPulseObserver(instance())
        }

        bind<Observer>(tag = "decrementDelay") with provider {
            DecrementDelayObserver(instance())
        }

        bind<Observer>(tag = "decrementItemDecayTimer") with provider {
            DecrementItemDecayTimerObserver(instance())
        }

        bind<Observer>(tag = "decreaseThirstAndHunger") with provider {
            DecreaseThirstAndHungerObserver(instance(), instance())
        }

        bind<Observer>(tag = "logTick") with provider {
            LogTickObserver(instance())
        }

        bind<Observer>(tag = "changeWeather") with provider {
            ChangeWeatherObserver(instance())
        }

        bind<Observer>(tag = "regenMobs") with provider {
            RegenMobsObserver(instance())
        }

        bind<Observer>(tag = "moveMobsOnTick") with provider {
            MoveMobsOnTickObserver(instance())
        }

        bind<Observer>(tag = "scavengerCollectsItem") with provider {
            ScavengerCollectsItemsObserver(instance(), instance(), instance())
        }

        bind<Observer>(tag = "generateMobs") with provider {
            GenerateMobsObserver(instance())
        }

        bind<Observer>(tag = "fastHealing") with provider {
            FastHealingObserver()
        }

        bind<Observer>(tag = "meditation") with provider {
            MeditationObserver()
        }

        bind<Observer>(tag = "guardAttacksAggroMobs") with provider {
            GuardAttacksAggroMobsObserver(instance())
        }

        bind<Observer>(tag = "wimpy") with provider {
            WimpyObserver(instance())
        }

        bind<Observer>(tag = "enhancedDamage") with provider {
            EnhancedDamageObserver()
        }

        bind<Observer>(tag = "secondAttack") with provider {
            SecondAttackObserver()
        }

        bind<Observer>(tag = "thirdAttack") with provider {
            ThirdAttackObserver()
        }

        bind<Observer>(tag = "grantExperienceOnKill") with provider {
            GrantExperienceOnKillObserver(instance())
        }

        bind<Observer>(tag = "transferGoldOnKill") with provider {
            TransferGoldOnKillObserver(instance())
        }

        bind<Observer>(tag = "growResources") with provider {
            GrowResourcesObserver(instance())
        }

        bind<Observer>(tag = "generateGrass") with provider {
            GenerateGrassObserver(instance())
        }

        bind<Observer>(tag = "processClientBuffers") with provider {
            ProcessClientBuffersObserver(instance(), instance(), instance(), instance(), instance())
        }

        bind<Observer>(tag = "readIntoClientBuffers") with provider {
            ReadIntoClientBuffersObserver(instance())
        }

        bind<Observer>(tag = "removeDisconnectedClients") with provider {
            RemoveDisconnectedClients(instance())
        }

        bind<Observer>(tag = "timeServiceLoop") with provider {
            TimeServiceLoopObserver(instance())
        }

        bind<ObserverList>() with singleton {
            mapOf(
                Pair(EventType.GAME_START, listOf(
                    instance(tag = "logoutAllPlayersOnStartup")
                )),
                Pair(EventType.GAME_LOOP, listOf(
                    instance(tag = "processClientBuffers"),
                    instance(tag = "readIntoClientBuffers"),
                    instance(tag = "removeDisconnectedClients"),
                    instance(tag = "timeServiceLoop")
                )),
                Pair(EventType.CLIENT_CONNECTED, listOf(
                    instance(tag = "clientConnected")
                )),
                Pair(EventType.SEND_MESSAGE_TO_ROOM, listOf(
                    instance(tag = "sendMessageToRoom")
                )),
                Pair(EventType.CLIENT_LOGGED_IN, listOf(
                    instance(tag = "logPlayerIn")
                )),
                Pair(EventType.CLIENT_DISCONNECTED, listOf(
                    instance(tag = "logPlayerOut")
                )),
                Pair(EventType.SOCIAL, listOf(
                    instance(tag = "social")
                )),
                Pair(EventType.PULSE, listOf(
                    instance(tag = "proceedFights"),
                    instance(tag = "decrementAffectTimeout"),
                    instance(tag = "pruneDeadMobs")
                )),
                Pair(EventType.TICK, listOf(
                    instance(tag = "decrementDelay"),
                    instance(tag = "decrementItemDecayTimer"),
                    instance(tag = "decreaseThirstAndHunger"),
                    instance(tag = "logTick"),
                    instance(tag = "changeWeather"),
                    instance(tag = "regenMobs"),
                    instance(tag = "moveMobsOnTick"),
                    instance(tag = "scavengerCollectsItem"),
                    instance(tag = "generateMobs"),
                    instance(tag = "growResources"),
                    instance(tag = "generateGrass")
                )),
                Pair(EventType.REGEN, listOf(
                    instance(tag = "fastHealing"),
                    instance(tag = "meditation")
                )),
                Pair(EventType.FIGHT_STARTED, listOf(
                    instance(tag = "guardAttacksAggroMobs")
                )),
                Pair(EventType.FIGHT_ROUND, listOf(
                    instance(tag = "wimpy"),
                    instance(tag = "enhancedDamage"),
                    instance(tag = "secondAttack"),
                    instance(tag = "thirdAttack")
                )),
                Pair(EventType.KILL, listOf(
                    instance(tag = "grantExperienceOnKill"),
                    instance(tag = "transferGoldOnKill")
                ))
            )
        }
    }
}
