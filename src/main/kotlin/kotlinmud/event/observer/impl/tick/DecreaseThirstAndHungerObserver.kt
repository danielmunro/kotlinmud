package kotlinmud.event.observer.impl.tick

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.io.service.ServerService

class DecreaseThirstAndHungerObserver(
    private val serverService: ServerService
) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        serverService.getLoggedInClients().forEach { client ->
            client.mob?.let {
                it.hunger -= 1
                it.thirst -= 1
                if (it.hunger <= 0) {
                    client.writePrompt("You are hungry.")
                }
                if (it.thirst <= 0) {
                    client.writePrompt("You are thirsty.")
                }
            }
        }
    }
}
