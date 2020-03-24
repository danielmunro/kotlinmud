package kotlinmud.event.observer

import kotlinmud.event.observer.impl.ClientConnectedObserver
import kotlinmud.event.observer.impl.DecrementAffectTimeoutTickObserver
import kotlinmud.event.observer.impl.DecrementDelayObserver
import kotlinmud.event.observer.impl.GrantExperienceOnKillObserver
import kotlinmud.event.observer.impl.GuardAttacksAggroMobsObserver
import kotlinmud.event.observer.impl.IncreaseThirstAndHungerObserver
import kotlinmud.event.observer.impl.InputReceivedObserver
import kotlinmud.event.observer.impl.LogTickObserver
import kotlinmud.event.observer.impl.MoveMobsOnTickObserver
import kotlinmud.event.observer.impl.ProceedFightsPulseObserver
import kotlinmud.event.observer.impl.PruneDeadMobsPulseObserver
import kotlinmud.event.observer.impl.RegenMobsObserver
import kotlinmud.event.observer.impl.RespawnTickObserver
import kotlinmud.event.observer.impl.ScavengerCollectsItemsObserver
import kotlinmud.event.observer.impl.SendMessageToRoomObserver
import kotlinmud.event.observer.impl.SocialDistributorObserver
import kotlinmud.event.observer.impl.TransferGoldOnKillObserver
import kotlinmud.event.observer.impl.WimpyObserver
import kotlinmud.io.Server
import kotlinmud.service.EventService
import kotlinmud.service.MobService
import kotlinmud.service.RespawnService

fun createObservers(server: Server, mobService: MobService, eventService: EventService, respawnService: RespawnService): List<Observer> {
    return listOf(
        // client/server observers
        ClientConnectedObserver(mobService),
        SendMessageToRoomObserver(server, mobService),
        InputReceivedObserver(mobService),

        // time
        ProceedFightsPulseObserver(mobService),
        DecrementAffectTimeoutTickObserver(mobService),
        DecrementDelayObserver(mobService),

        // game logic
        LogTickObserver(),
        PruneDeadMobsPulseObserver(mobService),
        RespawnTickObserver(respawnService),
        SocialDistributorObserver(server, mobService),

        // mobs
        WimpyObserver(mobService),
        GrantExperienceOnKillObserver(server),
        TransferGoldOnKillObserver(),
        IncreaseThirstAndHungerObserver(mobService, server),
        RegenMobsObserver(mobService),

        // job behaviors
        MoveMobsOnTickObserver(mobService, eventService),
        ScavengerCollectsItemsObserver(mobService, eventService),
        GuardAttacksAggroMobsObserver(mobService)
    )
}
