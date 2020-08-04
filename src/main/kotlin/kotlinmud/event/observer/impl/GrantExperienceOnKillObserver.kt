package kotlinmud.event.observer.impl

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.KillEvent
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType
import kotlinmud.io.service.ServerService

class GrantExperienceOnKillObserver(
    private val serverService: ServerService
) : Observer {
    override val eventType: EventType = EventType.KILL

    override fun <T> processEvent(event: Event<T>) {
        val killEvent = event.subject as KillEvent
        val victor = killEvent.victor
        val vanquished = killEvent.vanquished
        if (victor.isNpc) {
            return
        }
        val experience = getBaseExperience(vanquished.level - victor.level)
            .let {
            when {
                victor.level < 11 -> 15 * it / (victor.level + 4)
                victor.level > 40 -> 40 * it / (victor.level - 1)
                else -> it
            }
        }
        val addExperience = victor.mobCard?.addExperience(victor.level, experience)
        serverService.getClientForMob(victor)?.let { client ->
            client.writePrompt("you gain $experience experience.")
            addExperience?.levelGained?.let {
                client.writePrompt("you gained a level!")
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
