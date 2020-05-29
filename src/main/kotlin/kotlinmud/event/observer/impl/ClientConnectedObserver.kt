package kotlinmud.event.observer.impl

import kotlinmud.action.ActionService
import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.event.ClientConnectedEvent
import kotlinmud.event.observer.Observer
import kotlinmud.io.NIOClient
import kotlinmud.io.Request
import kotlinmud.mob.Appetite
import kotlinmud.mob.MobService
import kotlinmud.player.PlayerService
import kotlinmud.player.model.MobCardBuilder
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

    private fun loginDummyMob(client: NIOClient) {
        val mob = fixtureService.createMobBuilder()
            .name("foo")
            .isNpc(false)
            .trains(5)
            .practices(10)
            .gold(100)
            .build()
        mobService.addPlayerMob(mob)
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
        mobService.persistPlayerMobs()
        playerService.persist()
        client.mob = mob
        actionService.run(Request(client.mob!!, "look", mobService.getStartRoom())).let {
            client.writePrompt(it.message.toActionCreator)
        }
    }

    private fun addPreAuthClient(client: NIOClient) {
        playerService.addPreAuthClient(client)
    }
}
