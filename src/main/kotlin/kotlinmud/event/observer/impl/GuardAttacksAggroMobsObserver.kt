package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.event.FightStartedEvent
import kotlinmud.event.observer.Observer
import kotlinmud.io.Message
import kotlinmud.mob.JobType
import kotlinmud.mob.fight.Fight
import kotlinmud.service.MobService

class GuardAttacksAggroMobsObserver(private val mobService: MobService) : Observer {
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
                    Message(
                        "You scream and attack ${fight.aggressor}!",
                        "$it screams and attacks you!",
                        "$it screams and attacks ${fight.aggressor}"
                    ),
                    room,
                    it,
                    fight.aggressor
                )
            }
    }
}
