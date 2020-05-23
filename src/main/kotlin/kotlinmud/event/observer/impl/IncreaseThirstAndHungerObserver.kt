package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.observer.Observer
import kotlinmud.io.NIOServer
import kotlinmud.mob.MobService

class IncreaseThirstAndHungerObserver(private val mobService: MobService, private val server: NIOServer) :
    Observer {
    override val eventType: EventType = EventType.TICK

    override fun <T> processEvent(event: Event<T>) {
        val mobs = mobService.getMobRooms().filter { !it.mob.isNpc }.map { it.mob }
        val clients = server.getClientsFromMobs(mobs)
        clients.forEach {
            with(it.mob!!) {
                appetite.decrement()
                if (appetite.isHungry()) {
                    it.writePrompt("You are hungry.")
                }
                if (appetite.isThirsty()) {
                    it.writePrompt("You are thirsty.")
                }
            }
        }
    }
}
