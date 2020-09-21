package kotlinmud.event.observer.impl.tick

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType
import kotlinmud.io.model.Client
import kotlinmud.io.service.ServerService
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.service.MobService

class DecreaseThirstAndHungerObserver(
    private val serverService: ServerService,
    private val mobService: MobService
) : Observer {
    override val eventType: EventType = EventType.TICK

    override fun <T> processEvent(event: Event<T>) {
        serverService.getClients().forEach { client ->
            client.mob?.let {
                decrease(client, it)
            }
        }
    }

    private fun decrease(client: Client, mob: MobDAO) {
        mobService.decreaseThirstAndHunger(mob.name)?.let {
            if (it.hunger <= 0) {
                client.writePrompt("You are hungry.")
            }
            if (it.thirst <= 0) {
                client.writePrompt("You are thirsty.")
            }
        }
    }
}
