package kotlinmud.event.observer.impl.tick

import kotlinmud.event.impl.Event
import kotlinmud.io.service.ServerService
import kotlinmud.mob.service.MobService

fun decreaseThirstAndHungerEvent(serverService: ServerService, mobService: MobService, event: Event<*>) {
    serverService.getClients().forEach { client ->
        client.mob?.let {
            mobService.decreaseThirstAndHunger(it.name)?.let { card ->
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
