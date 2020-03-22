package kotlinmud.event.observer

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.mob.fight.Fight

class TransferGoldOnKillObserver : Observer {
    override val eventTypes: List<EventType> = listOf(EventType.KILL)

    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        val fight = event.subject as Fight
        val winner = fight.getWinner()!!
        val loser = fight.getOpponentFor(winner)!!

        winner.gold += loser.gold
        loser.gold = 0

        @Suppress("UNCHECKED_CAST")
        return EventResponse(event.subject as A)
    }
}
