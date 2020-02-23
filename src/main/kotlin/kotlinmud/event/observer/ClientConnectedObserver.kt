package kotlinmud.event.observer

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.event.WrongEventTypeException
import kotlinmud.event.event.ClientConnectedEvent
import kotlinmud.service.FixtureService
import kotlinmud.service.MobService

class ClientConnectedObserver(private val mobService: MobService) : Observer {
    override val eventTypes: List<EventType> = listOf(EventType.CLIENT_CONNECTED)
    private val fixtureService = FixtureService()

    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        if (event.subject is ClientConnectedEvent) {
            val mob = fixtureService.createMob()
            mobService.addMob(mob)
            @Suppress("UNCHECKED_CAST")
            return EventResponse(mob as A)
        }
        throw WrongEventTypeException()
    }
}
