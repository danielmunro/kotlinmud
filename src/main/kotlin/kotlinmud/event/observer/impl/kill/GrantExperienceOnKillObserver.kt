package kotlinmud.event.observer.impl.kill

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.KillEvent
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType
import kotlinmud.io.model.Client
import kotlinmud.io.service.ServerService
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.helper.getExperienceGain
import kotlinmud.mob.model.AddExperience
import org.jetbrains.exposed.sql.transactions.transaction

class GrantExperienceOnKillObserver(private val serverService: ServerService) : Observer {
    override val eventType: EventType = EventType.KILL

    override fun <T> processEvent(event: Event<T>) {
        val killEvent = event.subject as KillEvent
        val victor = killEvent.victor
        val vanquished = killEvent.vanquished
        if (victor.isNpc) {
            return
        }
        val gain = getExperienceGain(victor, vanquished)
        val experienceAddedResponse = addExperience(victor, gain)
        serverService.getClientForMob(victor)?.let { sendClientUpdates(it, experienceAddedResponse) }
    }

    private fun addExperience(mob: MobDAO, amountOfExperienceGained: Int): AddExperience {
        return mob.mobCard!!.addExperience(mob.level, amountOfExperienceGained).also {
            if (it.levelGained) {
                transaction { mob.level += 1 }
            }
        }
    }

    private fun sendClientUpdates(client: Client, experienceAddedResponse: AddExperience) {
        client.write("you gain ${experienceAddedResponse.experienceAdded} experience.")
        if (experienceAddedResponse.levelGained) {
            client.writePrompt("you gained a level!")
        }
    }
}
