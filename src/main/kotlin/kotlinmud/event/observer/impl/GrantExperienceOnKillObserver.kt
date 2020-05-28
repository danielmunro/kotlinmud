package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.observer.Observer
import kotlinmud.io.NIOServer
import kotlinmud.mob.fight.Fight
import kotlinmud.player.PlayerService

class GrantExperienceOnKillObserver(
    private val playerService: PlayerService,
    private val server: NIOServer
) : Observer {
    override val eventType: EventType = EventType.KILL

    override fun <T> processEvent(event: Event<T>) {
        val fight = event.subject as Fight
        val winner = fight.getWinner()!!
        val killed = fight.getOpponentFor(winner)!!
        if (winner.isNpc) {
            return
        }
        val experience = getBaseExperience(killed.level - winner.level)
            .let {
            when {
                winner.level < 11 -> 15 * it / (winner.level + 4)
                winner.level > 40 -> 40 * it / (winner.level - 1)
                else -> it
            }
        }
        playerService.findMobCardByName(winner.name)?.let {
            val addExperience = it.addExperience(winner.level, experience)
            server.getClientForMob(winner)?.let { client ->
                client.writePrompt("you gain $experience experience.")
                if (addExperience.levelGained) {
                    client.writePrompt("you gained a level!")
                }
            }
        }
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
