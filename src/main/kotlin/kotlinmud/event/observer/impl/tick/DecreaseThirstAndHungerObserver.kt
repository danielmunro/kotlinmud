package kotlinmud.event.observer.impl.tick

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.io.service.ServerService
import kotlinmud.mob.service.MobService

class DecreaseThirstAndHungerObserver(
    private val serverService: ServerService,
    private val mobService: MobService
) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        serverService.getLoggedInClients().forEach { client ->
            mobService.decreaseThirstAndHunger(client.mob!!.name)?.let { card ->
                if (card.hunger <= 0) {
                    client.writePrompt("You are hungry.")
                }
                if (card.thirst <= 0) {
                    client.writePrompt("You are thirsty.")
                }
            }
        }
    }
}
