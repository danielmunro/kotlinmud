package kotlinmud.player.auth.service

import kotlinmud.io.model.Client
import kotlinmud.mob.model.PlayerMob
import kotlinmud.mob.service.MobService
import kotlinmud.player.auth.model.CreationFunnel
import kotlinmud.player.dao.PlayerDAO
import kotlinmud.player.service.PlayerService
import kotlinmud.room.service.RoomService
import kotlinmud.player.repository.findPlayerByEmail as findPlayerByEmailQuery

class AuthStepService(
    private val mobService: MobService,
    private val roomService: RoomService,
    private val playerService: PlayerService
) {
    private val creationFunnels = mutableListOf<CreationFunnel>()

    fun createCreationFunnel(email: String): CreationFunnel {
        return CreationFunnel(mobService, email)
    }

    fun addCreationFunnel(creationFunnel: CreationFunnel) {
        creationFunnel.mobRoom = roomService.getStartRoom()
        creationFunnels.add(creationFunnel)
    }

    fun findCreationFunnelForEmail(email: String): CreationFunnel? {
        return creationFunnels.find { it.email == email }
    }

    fun getCreationFunnelForEmail(email: String): CreationFunnel {
        return findCreationFunnelForEmail(email)!!
    }

    fun findPlayerMobByName(name: String): PlayerMob? {
        return playerService.rehydratePlayerMob(name)
    }

    fun createPlayer(emailAddress: String): PlayerDAO {
        playerService.createNewPlayerWithEmailAddress(emailAddress).also {
            sendOTP(it)
        }
        return findPlayerByEmail(emailAddress)!!
    }

    fun findPlayerByOTP(otp: String): PlayerDAO? {
        return playerService.findPlayerByOTP(otp)
    }

    fun findPlayerByEmail(email: String): PlayerDAO? {
        return findPlayerByEmailQuery(email)
    }

    fun sendOTP(player: PlayerDAO) {
        playerService.sendOTP(player)
    }

    fun loginClientAsPlayer(client: Client, player: PlayerDAO) {
        playerService.loginClientAsPlayer(client, player)
    }
}
