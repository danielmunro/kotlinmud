package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.observer.Observer
import kotlinmud.mob.fight.Fight

class TransferGoldOnKillObserver : Observer {
    override val eventType: EventType = EventType.KILL

    override fun <T> processEvent(event: Event<T>) {
        val fight = event.subject as Fight
        val winner = fight.getWinner()!!
        val loser = fight.getOpponentFor(winner)!!

        winner.gold += loser.gold
        loser.gold = 0
    }
}
