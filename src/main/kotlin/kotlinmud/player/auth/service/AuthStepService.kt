package kotlinmud.player.auth.service

import kotlinmud.io.model.Client
import kotlinmud.player.auth.model.CreationFunnel
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.player.dao.PlayerDAO
import kotlinmud.player.service.PlayerService
import kotlinmud.room.repository.findRoomById
import kotlinmud.room.repository.findStartRoom
import kotlinmud.player.repository.findPlayerByEmail as findPlayerByEmailQuery

class AuthStepService(private val playerService: PlayerService) {
    private val creationFunnels = mutableListOf<CreationFunnel>()

    fun addCreationFunnel(creationFunnel: CreationFunnel) {
        creationFunnel.mobRoom = findStartRoom() ?: findRoomById(1)
        creationFunnels.add(creationFunnel)
    }

    fun findCreationFunnelForEmail(email: String): CreationFunnel? {
        return creationFunnels.find { it.email == email }
    }

    fun getCreationFunnelForEmail(email: String): CreationFunnel {
        return findCreationFunnelForEmail(email)!!
    }

    fun findMobCardByName(name: String): MobCardDAO? {
        return playerService.findMobCardByName(name)
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
