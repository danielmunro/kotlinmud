package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.observer.Observer
import kotlinmud.io.service.ServerService
import kotlinmud.player.service.PlayerService

class IncreaseThirstAndHungerObserver(
    private val playerService: PlayerService,
    private val serverService: ServerService
) : Observer {
    override val eventType: EventType = EventType.TICK

    override fun <T> processEvent(event: Event<T>) {
        val mobCards = playerService.getMobCards()
        serverService.getClients().forEach { client ->
            mobCards.find { it.mobName == client.mob!!.name }?.let {
                val appetite = it.appetite
                appetite.decrement()
                if (appetite.isHungry()) {
                    client.writePrompt("You are hungry.")
                }
                if (appetite.isThirsty()) {
                    client.writePrompt("You are thirsty.")
                }
            }
        }
    }
}
