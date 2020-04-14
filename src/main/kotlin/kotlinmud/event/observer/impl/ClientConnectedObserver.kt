package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.event.ClientConnectedEvent
import kotlinmud.event.observer.Observer
import kotlinmud.service.FixtureService
import kotlinmud.service.MobService

class ClientConnectedObserver(private val mobService: MobService) :
    Observer {
    override val eventType: EventType = EventType.CLIENT_CONNECTED
    private val fixtureService = FixtureService()

    override fun <T> processEvent(event: Event<T>) {
        val mob = fixtureService.createMobBuilder()
            .isNpc(false)
            .trains(5)
            .practices(10)
            .gold(100)
            .build()
        mobService.addMob(mob)
        val connectedEvent = event.subject as ClientConnectedEvent
        val client = connectedEvent.client
        client.mob = mob
    }
}
