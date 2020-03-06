package kotlinmud.event.observer

import kotlinmud.io.Server
import kotlinmud.service.EventService
import kotlinmud.service.MobService

fun createObservers(server: Server, mobService: MobService, eventService: EventService): List<Observer> {
    return listOf(
        ClientConnectedObserver(mobService),
        SendMessageToRoomObserver(server, mobService),
        InputReceivedObserver(mobService),
        ProceedFightsPulseObserver(mobService),
        DecrementAffectTimeoutTickObserver(mobService),
        DecrementDelayObserver(mobService),
        PruneDeadMobsPulseObserver(mobService),
        RespawnTickObserver(mobService),
        LogTickObserver(),
        SocialDistributorObserver(server, mobService),
        MoveScavengersOnTickObserver(mobService, eventService),
        ScavengerCollectsItemsObserver(mobService, eventService)
    )
}
