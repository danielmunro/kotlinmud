package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.event.ClientConnectedEvent
import kotlinmud.event.observer.Observer
import kotlinmud.io.NIOClient
import kotlinmud.io.Request
import kotlinmud.service.ActionService
import kotlinmud.service.FixtureService
import kotlinmud.service.MobService
import kotlinmud.service.PlayerService

class ClientConnectedObserver(
    private val playerService: PlayerService,
    private val mobService: MobService,
    private val actionService: ActionService
) : Observer {
    override val eventType: EventType = EventType.CLIENT_CONNECTED
    private val fixtureService = FixtureService()

    override fun <T> processEvent(event: Event<T>) {
        val connectedEvent = event.subject as ClientConnectedEvent
        val client = connectedEvent.client
        loginDummyMob(client)
        addPreAuthClient(client)
    }

    private fun loginDummyMob(client: NIOClient) {
        val mob = fixtureService.createMobBuilder()
            .isNpc(false)
            .trains(5)
            .practices(10)
            .gold(100)
            .build()
        mobService.addMob(mob)
        client.mob = mob
        actionService.run(Request(client.mob!!, "look", mobService.getStartRoom())).let {
            client.writePrompt(it.message.toActionCreator)
        }
    }

    private fun addPreAuthClient(client: NIOClient) {
        playerService.addPreAuthClient(client)
    }
}
