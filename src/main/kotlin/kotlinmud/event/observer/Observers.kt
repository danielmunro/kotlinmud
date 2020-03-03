package kotlinmud.event.observer

import kotlinmud.io.Server
import kotlinmud.service.MobService

fun createObservers(server: Server, mobService: MobService): List<Observer> {
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
        SocialDistributorObserver(server, mobService))
}
