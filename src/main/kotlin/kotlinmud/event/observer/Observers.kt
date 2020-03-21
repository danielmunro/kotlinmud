package kotlinmud.event.observer

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
        MoveScavengersOnTickObserver(mobService, eventService),
        ScavengerCollectsItemsObserver(mobService, eventService),
        GuardAttacksAggroMobsObserver(mobService)
    )
}
