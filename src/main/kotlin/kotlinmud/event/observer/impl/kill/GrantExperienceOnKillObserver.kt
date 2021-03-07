package kotlinmud.event.observer.impl.kill

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.KillEvent
import kotlinmud.event.observer.type.Observer
import kotlinmud.io.model.Client
import kotlinmud.io.service.ServerService
import kotlinmud.mob.helper.getExperienceGain
import kotlinmud.mob.model.AddExperience
import kotlinmud.mob.model.PlayerMob

class GrantExperienceOnKillObserver(private val serverService: ServerService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        val killEvent = event.subject as KillEvent
        val victor = killEvent.victor
        val vanquished = killEvent.vanquished
        val gain = getExperienceGain(victor, vanquished)
        if (victor is PlayerMob) {
            addExperience(victor, gain)?.let { addExperience ->
                serverService.getClientForMob(victor)?.let { sendClientUpdates(it, addExperience) }
            }
        }
    }

    private fun addExperience(mob: PlayerMob, amountOfExperienceGained: Int): AddExperience? {
        return mob.addExperience(mob.level, amountOfExperienceGained).also {
            if (it.levelGained) {
                mob.level += 1
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
