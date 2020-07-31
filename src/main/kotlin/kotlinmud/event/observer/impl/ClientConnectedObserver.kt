package kotlinmud.event.observer.impl

import kotlinmud.action.service.ActionService
import kotlinmud.event.impl.ClientConnectedEvent
import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType
import kotlinmud.io.model.Client
import kotlinmud.io.model.Request
import kotlinmud.mob.factory.mobBuilder
import kotlinmud.mob.service.MobService
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.player.service.PlayerService
import org.jetbrains.exposed.sql.transactions.transaction

class ClientConnectedObserver(
    private val playerService: PlayerService,
    private val mobService: MobService,
    private val actionService: ActionService
) : Observer {
    override val eventType: EventType = EventType.CLIENT_CONNECTED

    override fun <T> processEvent(event: Event<T>) {
        val connectedEvent = event.subject as ClientConnectedEvent
        val client = connectedEvent.client
        loginDummyMob(client)
//        addPreAuthClient(client)
//        client.write("email: ")
    }

    private fun loginDummyMob(client: Client) {
        val mob = mobBuilder("foo", mobService.getStartRoom())
        playerService.createNewPlayerWithEmailAddress("dan@danmunro.com")
        val card = transaction {
            MobCardDAO.new {
                trains = 5
                practices = 5
                hunger = mob.race.maxAppetite
                thirst = mob.race.maxThirst
                experiencePerLevel = 1000
                this.mob = mob
                respawnRoom = mobService.getStartRoom()
            }
        }
        client.mob = mob
        actionService.run(
            Request(
                client.mob!!,
                "look",
                transaction { card.respawnRoom }
            )
        ).let {
            client.writePrompt(it.message.toActionCreator)
        }
    }

    private fun addPreAuthClient(client: Client) {
        playerService.addPreAuthClient(client)
    }
}
