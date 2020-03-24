package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.event.observer.Observer
import kotlinmud.io.Message
import kotlinmud.mob.Mob
import kotlinmud.mob.fight.Round
import kotlinmud.service.MobService

class WimpyObserver(private val mobService: MobService) : Observer {
    override val eventTypes: List<EventType> = listOf(EventType.FIGHT_ROUND)

    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        val round = event.subject as Round

        if (!checkWimpy(round.defender)) {
            checkWimpy(round.attacker)
        }

        @Suppress("UNCHECKED_CAST")
        return EventResponse(event.subject as A)
    }

    private fun checkWimpy(mob: Mob): Boolean {
        val room = mobService.getRoomForMob(mob)
        if (mob.wimpy > mob.hp && room.exits.size > 0) {
            mobService.endFightFor(mob)
            val exit = room.exits.random()
            mobService.sendMessageToRoom(Message(
                "you flee heading ${exit.direction.value}!",
                "$mob flees heading ${exit.direction.value}!"),
                room,
                mob
            )
            mobService.putMobInRoom(mob, exit.destination)
            mobService.sendMessageToRoom(Message(
                "",
                "$mob arrives."),
                exit.destination,
                mob
            )
            return true
        }
        return false
    }
}
