package kotlinmud.event.observer.impl

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType
import kotlinmud.io.service.ServerService
import org.jetbrains.exposed.sql.transactions.transaction

class IncreaseThirstAndHungerObserver(
    private val serverService: ServerService
) : Observer {
    override val eventType: EventType = EventType.TICK

    override fun <T> processEvent(event: Event<T>) {
        serverService.getClients().forEach { client ->
            transaction {
                client.mob?.mobCard?.let {
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
}
