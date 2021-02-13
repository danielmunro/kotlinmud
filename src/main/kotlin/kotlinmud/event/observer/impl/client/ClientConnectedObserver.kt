package kotlinmud.event.observer.impl.client

import kotlinmud.app.Environment
import kotlinmud.event.impl.ClientConnectedEvent
import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.specialization.impl.Warrior
import kotlinmud.player.auth.model.CreationFunnel
import kotlinmud.player.dao.PlayerDAO
import kotlinmud.player.repository.findPlayerByEmail
import kotlinmud.player.service.PlayerService
import kotlinmud.room.repository.findRoomByCanonicalId
import kotlinmud.type.CanonicalId
import org.jetbrains.exposed.sql.transactions.transaction

class ClientConnectedObserver(private val playerService: PlayerService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        with(event.subject as ClientConnectedEvent) {
            if (Environment.isDev()) {
                val player = findPlayerByEmail("dan@danmunro.com") ?: transaction {
                    PlayerDAO.new {
                        email = "dan@danmunro.com"
                        name = "foo"
                    }
                }
                playerService.loginClientAsPlayer(this.client, player)
                val funnel = CreationFunnel("dan@danmunro.com")
                funnel.mobName = "foo"
                funnel.mobRace = Human()
                funnel.mobRoom = findRoomByCanonicalId(CanonicalId.FIND_RECRUITER_PRAETORIAN_GUARD)
                funnel.specialization = Warrior()
                val mob = funnel.build()
                this.client.mob = mob
                return
            }
            playerService.addPreAuthClient(this.client)
            this.client.write("email: ")
        }
    }
}
