package kotlinmud.event.observer

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.event.event.ClientConnectedEvent
import kotlinmud.event.response.ClientConnectedResponse
import kotlinmud.service.FixtureService
import kotlinmud.service.MobService

class ClientConnectedObserver(private val mobService: MobService) : Observer {
    override val eventTypes: List<EventType> = listOf(EventType.CLIENT_CONNECTED)
    private val fixtureService = FixtureService()

    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        if (event.subject is ClientConnectedEvent) {
            val mob = fixtureService.createMob()
            mobService.respawnMobToStartRoom(mob)
            @Suppress("UNCHECKED_CAST")
            return ClientConnectedResponse(mob as A)
        }
        throw Exception()
    }
}
