package kotlinmud.event.observer

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.event.WrongEventTypeException
import kotlinmud.event.event.FightStartedEvent
import kotlinmud.io.Message
import kotlinmud.mob.JobType
import kotlinmud.mob.fight.Fight
import kotlinmud.service.MobService

class GuardAttacksAggroMobsObserver(private val mobService: MobService) : Observer {
    override val eventTypes: List<EventType> = listOf(EventType.FIGHT_STARTED)

    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        if (event.subject !is FightStartedEvent) {
            throw WrongEventTypeException()
        }

        val room = mobService.getRoomForMob(event.subject.aggressor)
        mobService.getMobsForRoom(room).filter {
                it != event.subject.aggressor &&
                        it != event.subject.defender &&
                        it.job == JobType.GUARD &&
                        mobService.findFightForMob(it) == null
            }.forEach {
                mobService.addFight(Fight(it, event.subject.aggressor))
                mobService.sendMessageToRoom(
                    Message(
                        "You scream and attack ${event.subject.aggressor}!",
                        "$it screams and attacks you!",
                        "$it screams and attacks ${event.subject.aggressor}"
                    ),
                    room,
                    it,
                    event.subject.aggressor
                )
            }

        @Suppress("UNCHECKED_CAST")
        return EventResponse(event as A)
    }
}
