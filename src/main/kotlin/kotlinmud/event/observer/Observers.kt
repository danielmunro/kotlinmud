package kotlinmud.event.observer

import kotlinmud.event.observer.impl.ChangeWeatherObserver
import kotlinmud.event.observer.impl.ClientConnectedObserver
import kotlinmud.event.observer.impl.DecrementAffectTimeoutTickObserver
import kotlinmud.event.observer.impl.DecrementDelayObserver
import kotlinmud.event.observer.impl.DecrementItemDecayTimerObserver
import kotlinmud.event.observer.impl.GrantExperienceOnKillObserver
import kotlinmud.event.observer.impl.GuardAttacksAggroMobsObserver
import kotlinmud.event.observer.impl.IncreaseThirstAndHungerObserver
import kotlinmud.event.observer.impl.LogTickObserver
import kotlinmud.event.observer.impl.MoveMobsOnTickObserver
import kotlinmud.event.observer.impl.ProceedFightsPulseObserver
import kotlinmud.event.observer.impl.PruneDeadMobsPulseObserver
import kotlinmud.event.observer.impl.RegenMobsObserver
import kotlinmud.event.observer.impl.RemoveMobOnClientDisconnectObserver
import kotlinmud.event.observer.impl.RespawnTickObserver
import kotlinmud.event.observer.impl.SaveTimeObserver
import kotlinmud.event.observer.impl.SaveWorldObserver
import kotlinmud.event.observer.impl.ScavengerCollectsItemsObserver
import kotlinmud.event.observer.impl.SendMessageToRoomObserver
import kotlinmud.event.observer.impl.SocialDistributorObserver
import kotlinmud.event.observer.impl.TransferGoldOnKillObserver
import kotlinmud.event.observer.impl.WimpyObserver
import kotlinmud.fs.saver.WorldSaver
import kotlinmud.io.NIOServer
import kotlinmud.service.ActionService
import kotlinmud.service.EventService
import kotlinmud.service.ItemService
import kotlinmud.service.MobService
import kotlinmud.service.PlayerService
import kotlinmud.service.RespawnService
import kotlinmud.service.TimeService
import kotlinmud.service.WeatherService

typealias Observers = List<Observer>

fun createObservers(
    server: NIOServer,
    mobService: MobService,
    eventService: EventService,
    respawnService: RespawnService,
    weatherService: WeatherService,
    itemService: ItemService,
    worldSaver: WorldSaver,
    timeService: TimeService,
    actionService: ActionService,
    playerService: PlayerService
): Observers {
    return listOf(
        // client/server observers
        ClientConnectedObserver(playerService, mobService, actionService),
        SendMessageToRoomObserver(server, mobService),
        RemoveMobOnClientDisconnectObserver(mobService),

        // time
        ProceedFightsPulseObserver(mobService),
        DecrementAffectTimeoutTickObserver(mobService),
        DecrementDelayObserver(mobService),
        DecrementItemDecayTimerObserver(itemService),
        SaveWorldObserver(worldSaver),
        SaveTimeObserver(timeService),

        // game logic
        LogTickObserver(mobService, server),
        PruneDeadMobsPulseObserver(mobService),
        RespawnTickObserver(respawnService),
        SocialDistributorObserver(server, mobService),
        ChangeWeatherObserver(weatherService),

        // mobs
        WimpyObserver(mobService),
        GrantExperienceOnKillObserver(server),
        TransferGoldOnKillObserver(),
        IncreaseThirstAndHungerObserver(mobService, server),
        RegenMobsObserver(mobService),

        // job behaviors
        MoveMobsOnTickObserver(mobService, itemService, eventService),
        ScavengerCollectsItemsObserver(mobService, itemService, eventService),
        GuardAttacksAggroMobsObserver(mobService)
    )
}
