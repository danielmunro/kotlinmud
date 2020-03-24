package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.event.observer.Observer
import kotlinmud.service.FixtureService
import kotlinmud.service.MobService

class ClientConnectedObserver(private val mobService: MobService) :
    Observer {
    override val eventTypes: List<EventType> = listOf(EventType.CLIENT_CONNECTED)
    private val fixtureService = FixtureService()

    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        val mob = fixtureService.createMobBuilder()
            .setIsNpc(false)
            .setTrains(5)
            .setPractices(10)
            .setGold(100)
            .build()
        mobService.addMob(mob)
        @Suppress("UNCHECKED_CAST")
        return EventResponse(mob as A)
    }
}