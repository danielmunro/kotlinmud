package kotlinmud.player.authStep.service

import kotlinmud.io.model.Client
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.player.dao.PlayerDAO
import kotlinmud.player.service.PlayerService

class AuthStepService(private val playerService: PlayerService) {
    fun findMobCardByName(name: String): MobCardDAO? {
        return playerService.findMobCardByName(name)
    }

    fun createPlayer(emailAddress: String): PlayerDAO {
        return playerService.createNewPlayerWithEmailAddress(emailAddress)
    }

    fun findPlayerByOTP(otp: String): PlayerDAO? {
        return playerService.findPlayerByOTP(otp)
    }

    fun sendOTP(player: PlayerDAO) {
        playerService.sendOTP(player)
    }

    fun loginClientAsPlayer(client: Client, player: PlayerDAO) {
        playerService.loginClientAsPlayer(client, player)
    }
}
