package kotlinmud.event.observer.impl.mob

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.FightStartedEvent
import kotlinmud.event.observer.type.Observer
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.JobType

class GuardAttacksAggroMobsObserver(private val mobService: MobService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        val fight = event.subject as FightStartedEvent
        val room = fight.aggressor.room
        mobService.findMobsInRoom(room).forEach {
            if (it.job == JobType.GUARD && mobService.getMobFight(it) == null) {
                mobService.addFight(it, fight.aggressor)
                mobService.sendMessageToRoom(
                    MessageBuilder()
                        .toActionCreator("You scream and attack ${fight.aggressor}!")
                        .toTarget("$it screams and attacks you!")
                        .toObservers("$it screams and attacks ${fight.aggressor}")
                        .build(),
                    room,
                    it,
                    fight.aggressor
                )
            }
        }
    }
}
