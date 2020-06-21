package kotlinmud.event.observer.impl

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.FightStartedEvent
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.JobType

class GuardAttacksAggroMobsObserver(private val mobService: MobService) :
    Observer {
    override val eventType: EventType = EventType.FIGHT_STARTED

    override fun <T> processEvent(event: Event<T>) {
        val fight = event.subject as FightStartedEvent
        val room = mobService.getRoomForMob(fight.aggressor)
        mobService.getMobsForRoom(room).filter {
                it != fight.aggressor &&
                        it != fight.defender &&
                        it.job == JobType.GUARD &&
                        mobService.findFightForMob(it) == null
            }.forEach {
                mobService.addFight(Fight(it, fight.aggressor))
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
