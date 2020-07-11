package kotlinmud.event.observer.impl

import kotlinmud.action.service.ActionService
import kotlinmud.event.impl.ClientConnectedEvent
import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType
import kotlinmud.io.model.Client
import kotlinmud.io.model.Request
import kotlinmud.mob.factory.mobBuilder
import kotlinmud.mob.model.Appetite
import kotlinmud.mob.service.MobService
import kotlinmud.player.model.MobCardBuilder
import kotlinmud.player.service.PlayerService
import kotlinmud.service.FixtureService

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
//        addPreAuthClient(client)
//        client.write("email: ")
    }

    private fun loginDummyMob(client: Client) {
        val mob = mobBuilder("foo")
        mobService.addMob(mob)
        playerService.createNewPlayerWithEmailAddress("dan@danmunro.com")
        playerService.addMobCard(
            MobCardBuilder()
                .playerEmail("dan@danmunro.com")
                .mobName("foo")
                .trains(5)
                .practices(5)
                .appetite(Appetite(mob.race.maxAppetite, mob.race.maxThirst))
                .experiencePerLevel(1000)
                .build()
        )
        playerService.persist()
        client.mob = mob
        actionService.run(
            Request(
                client.mob!!,
                "look",
                mobService.getStartRoom()
            )
        ).let {
            client.writePrompt(it.message.toActionCreator)
        }
    }

    private fun addPreAuthClient(client: Client) {
        playerService.addPreAuthClient(client)
    }
}
