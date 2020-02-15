package kotlinmud.event.observer

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.event.response.ClientConnectedResponse
import kotlinmud.service.FixtureService

class ClientConnectedObserver : Observer {
    override val eventTypes: List<EventType> = listOf(EventType.CLIENT_CONNECTED)
    private val fixtureService = FixtureService()

    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        return ClientConnectedResponse(fixtureService.createMob() as A)
    }
}
