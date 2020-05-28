package kotlinmud.event.observer

import kotlinmud.action.ActionService
import kotlinmud.event.EventService
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
import kotlinmud.event.observer.impl.RespawnTickObserver
import kotlinmud.event.observer.impl.SaveTimeObserver
import kotlinmud.event.observer.impl.SaveVersionsObserver
import kotlinmud.event.observer.impl.SaveWorldObserver
import kotlinmud.event.observer.impl.ScavengerCollectsItemsObserver
import kotlinmud.event.observer.impl.SendMessageToRoomObserver
import kotlinmud.event.observer.impl.SocialDistributorObserver
import kotlinmud.event.observer.impl.TransferGoldOnKillObserver
import kotlinmud.event.observer.impl.WimpyObserver
import kotlinmud.io.ClientService
import kotlinmud.io.NIOServer
import kotlinmud.item.ItemService
import kotlinmud.mob.MobService
import kotlinmud.player.PlayerService
import kotlinmud.service.PersistenceService
import kotlinmud.service.RespawnService
import kotlinmud.service.TimeService
import kotlinmud.service.WeatherService
import kotlinmud.world.World

typealias Observers = List<Observer>

fun createObservers(
    server: NIOServer,
    mobService: MobService,
    eventService: EventService,
    respawnService: RespawnService,
    weatherService: WeatherService,
    itemService: ItemService,
    timeService: TimeService,
    actionService: ActionService,
    playerService: PlayerService,
    clientService: ClientService,
    persistenceService: PersistenceService,
    world: World
): Observers {
    return listOf(
        // client/server observers
        ClientConnectedObserver(playerService, mobService, actionService),
        SendMessageToRoomObserver(server, mobService),
        RemoveMobOnClientDisconnectObserver(mobService),
        LogPlayerInObserver(mobService),

        // time
        ProceedFightsPulseObserver(mobService),
        DecrementAffectTimeoutTickObserver(mobService),
        DecrementDelayObserver(clientService),
        DecrementItemDecayTimerObserver(itemService),
        SaveWorldObserver(persistenceService, playerService, mobService, world),
        SaveTimeObserver(timeService, persistenceService),

        // game logic
        LogTickObserver(mobService, server),
        PruneDeadMobsPulseObserver(mobService),
        RespawnTickObserver(respawnService),
        SocialDistributorObserver(server, mobService),
        ChangeWeatherObserver(weatherService),
        SaveVersionsObserver(persistenceService),

        // mobs
        WimpyObserver(mobService),
        GrantExperienceOnKillObserver(playerService, server),
        TransferGoldOnKillObserver(),
        IncreaseThirstAndHungerObserver(mobService, server),
        RegenMobsObserver(mobService),

        // job behaviors
        MoveMobsOnTickObserver(mobService, itemService, eventService),
        ScavengerCollectsItemsObserver(mobService, itemService, eventService),
        GuardAttacksAggroMobsObserver(mobService)
    )
}
