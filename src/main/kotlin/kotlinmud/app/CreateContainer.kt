package kotlinmud.app

import java.net.ServerSocket
import kotlinmud.action.helper.createActionContextBuilder
import kotlinmud.action.helper.createActionsList
import kotlinmud.action.service.ActionService
import kotlinmud.action.service.ContextBuilderService
import kotlinmud.biome.helper.createBiomes
import kotlinmud.db.createConnection
import kotlinmud.event.impl.Event
import kotlinmud.event.observer.impl.SocialDistributorObserver
import kotlinmud.event.observer.impl.client.clientConnectedEvent
import kotlinmud.event.observer.impl.client.logPlayerInEvent
import kotlinmud.event.observer.impl.guardAttacksAggroMobEvent
import kotlinmud.event.observer.impl.kill.GrantExperienceOnKillObserver
import kotlinmud.event.observer.impl.kill.transferGoldOnKillEvent
import kotlinmud.event.observer.impl.logoutAllPlayersOnStartupEvent
import kotlinmud.event.observer.impl.pulse.proceedFightsEvent
import kotlinmud.event.observer.impl.pulse.pruneDeadMobsEvent
import kotlinmud.event.observer.impl.regen.fastHealingEvent
import kotlinmud.event.observer.impl.regen.meditationEvent
import kotlinmud.event.observer.impl.round.enhancedDamageEvent
import kotlinmud.event.observer.impl.round.secondAttackEvent
import kotlinmud.event.observer.impl.round.wimpyEvent
import kotlinmud.event.observer.impl.sendMessageToRoomEvent
import kotlinmud.event.observer.impl.tick.changeWeatherEvent
import kotlinmud.event.observer.impl.tick.decreaseThirstAndHungerEvent
import kotlinmud.event.observer.impl.tick.decrementAffectTimeoutEvent
import kotlinmud.event.observer.impl.tick.decrementDelayEvent
import kotlinmud.event.observer.impl.tick.decrementItemDecayTimerEvent
import kotlinmud.event.observer.impl.tick.generateMobsEvent
import kotlinmud.event.observer.impl.tick.logTickObserver
import kotlinmud.event.observer.impl.tick.moveMobsOnTickEvent
import kotlinmud.event.observer.impl.tick.regenMobsEvent
import kotlinmud.event.observer.impl.tick.scavengerCollectsItemEvent
import kotlinmud.event.observer.type.ObserverV2
import kotlinmud.event.service.EventService
import kotlinmud.event.type.EventType
import kotlinmud.generator.service.FixtureService
import kotlinmud.generator.service.MobGeneratorService
import kotlinmud.helper.logger
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
import kotlinmud.time.service.TimeService
import kotlinmud.weather.service.WeatherService
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
        bind<ObserverV2>() with singleton {
            mapOf(
                Pair(EventType.GAME_START, listOf { event: Event<*> ->
                    logoutAllPlayersOnStartupEvent(instance<PlayerService>())
                }),
                Pair(EventType.CLIENT_CONNECTED, listOf { event: Event<*> ->
                    clientConnectedEvent(instance<PlayerService>(), event)
                }),
                Pair(EventType.SEND_MESSAGE_TO_ROOM, listOf { event: Event<*> ->
                    sendMessageToRoomEvent(instance<ServerService>(), event)
                }),
                Pair(EventType.CLIENT_LOGGED_IN, listOf { event: Event<*> ->
                    logPlayerInEvent(event)
                }),
                Pair(EventType.CLIENT_DISCONNECTED, listOf { event: Event<*> ->
                    logPlayerInEvent(event)
                }),
                Pair(EventType.SOCIAL, listOf { event: Event<*> ->
                    SocialDistributorObserver(instance<ServerService>()).event(event)
                }),
                Pair(EventType.PULSE, listOf(
                    { _: Event<*> ->
                        proceedFightsEvent(instance<MobService>())
                    },
                    {
                        decrementAffectTimeoutEvent(instance<MobService>())
                    },
                    {
                        pruneDeadMobsEvent(instance<MobService>())
                    }
                )),
                Pair(EventType.TICK, listOf(
                    { _: Event<*> ->
                        decrementDelayEvent(instance<ClientService>())
                    },
                    {
                        decrementItemDecayTimerEvent(instance<ItemService>())
                    },
                    {
                        decreaseThirstAndHungerEvent(instance<ServerService>(), instance<MobService>(), it)
                    },
                    {
                        logTickObserver(instance<ServerService>(), logger(this))
                    },
                    {
                        changeWeatherEvent(instance<WeatherService>())
                    },
                    {
                        regenMobsEvent(instance<MobService>())
                    },
                    {
                        moveMobsOnTickEvent(instance<MobService>())
                    },
                    {
                        scavengerCollectsItemEvent(instance<MobService>(), instance<ItemService>(), instance<EventService>())
                    },
                    {
                        generateMobsEvent(instance<MobGeneratorService>())
                    }
                )),
                Pair(EventType.REGEN, listOf(
                    { event: Event<*> ->
                        fastHealingEvent(event)
                    },
                    { event: Event<*> ->
                        meditationEvent(event)
                    }
                )),
                Pair(EventType.FIGHT_STARTED, listOf { event: Event<*> ->
                    guardAttacksAggroMobEvent(instance<MobService>(), event)
                }),
                Pair(EventType.FIGHT_ROUND, listOf(
                    { event: Event<*> ->
                        wimpyEvent(instance<MobService>(), event)
                    },
                    { event: Event<*> ->
                        enhancedDamageEvent(event)
                    },
                    { event: Event<*> ->
                        secondAttackEvent(event)
                    }
                )),
                Pair(EventType.KILL, listOf(
                    { event: Event<*> ->
                        GrantExperienceOnKillObserver(instance<ServerService>()).event(event)
                    },
                    { event: Event<*> ->
                        transferGoldOnKillEvent(instance<MobService>(), event)
                    }
                ))
            )
        }
    }
}
