package kotlinmud.event.observer

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.io.Server
import kotlinmud.mob.fight.Fight

class GrantExperienceOnKillObserver(private val server: Server) : Observer {
    override val eventTypes: List<EventType> = listOf(EventType.KILL)

    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        val fight = event.subject as Fight
        val winner = fight.getWinner()!!
        val killed = fight.getOpponentFor(winner)!!
        if (winner.isNpc) {
            @Suppress("UNCHECKED_CAST")
            return EventResponse(event.subject as A)
        }
        val experience = getBaseExperience(killed.level - winner.level).let {
            when {
                winner.level < 11 -> 15 * it / (winner.level + 4)
                winner.level > 40 -> 40 * it / (winner.level - 1)
                else -> it
            }
        }
        val addExperience = winner.addExperience(experience)
        server.getClientForMob(winner)?.let {
            it.write("you gain $experience experience.")
            if (addExperience.levelGained) {
                it.write("you gained a level!")
            }
        }

        @Suppress("UNCHECKED_CAST")
        return EventResponse(event.subject as A)
    }
}

fun getBaseExperience(levelDifference: Int): Int {
    return when (levelDifference) {
        -8 -> 2
        -7 -> 7
        -6 -> 13
        -5 -> 20
        -4 -> 26
        -3 -> 40
        -2 -> 60
        -1 -> 80
        0 -> 100
        1 -> 140
        2 -> 180
        3 -> 220
        4 -> 280
        5 -> 320
        else -> {
            if (levelDifference > 5) {
                return 320 + 30 * (levelDifference - 5)
            }
            0
        }
    }
}
