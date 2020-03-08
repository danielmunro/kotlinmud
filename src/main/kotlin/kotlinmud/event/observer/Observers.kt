package kotlinmud.event.observer

import kotlinmud.io.Server
import kotlinmud.service.EventService
import kotlinmud.service.MobService
import kotlinmud.service.RespawnService

fun createObservers(server: Server, mobService: MobService, eventService: EventService, respawnService: RespawnService): List<Observer> {
    return listOf(
        ClientConnectedObserver(mobService),
        SendMessageToRoomObserver(server, mobService),
        InputReceivedObserver(mobService),
        ProceedFightsPulseObserver(mobService),
        DecrementAffectTimeoutTickObserver(mobService),
        DecrementDelayObserver(mobService),
        PruneDeadMobsPulseObserver(mobService),
        RespawnTickObserver(respawnService),
        LogTickObserver(),
        SocialDistributorObserver(server, mobService),
        MoveScavengersOnTickObserver(mobService, eventService),
        ScavengerCollectsItemsObserver(mobService, eventService)
    )
}
