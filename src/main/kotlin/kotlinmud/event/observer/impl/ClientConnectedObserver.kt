package kotlinmud.event.observer.impl

import kotlinmud.action.impl.info.createLookAction
import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.event.ClientConnectedEvent
import kotlinmud.event.observer.Observer
import kotlinmud.io.Request
import kotlinmud.service.ActionService
import kotlinmud.service.FixtureService
import kotlinmud.service.MobService

class ClientConnectedObserver(
    private val mobService: MobService,
    private val actionService: ActionService
) : Observer {
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
        actionService.run(Request(mob, "look", mobService.getStartRoom())).let {
            client.writePrompt(it.message.toActionCreator)
        }
    }
}
