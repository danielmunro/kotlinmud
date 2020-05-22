package kotlinmud.player.authStep

import kotlinmud.io.NIOClient
import kotlinmud.player.PlayerService
import kotlinmud.player.model.Player

class AuthStepService(private val playerService: PlayerService) {
    fun createPlayer(emailAddress: String): Player {
        return playerService.createNewPlayerWithEmailAddress(emailAddress)
    }

    fun findPlayerByOTP(otp: String): Player? {
        return playerService.findPlayerByOTP(otp)
    }

    fun sendOTP(player: Player) {
        playerService.sendOTP(player)
    }

    fun loginClientAsPlayer(client: NIOClient, player: Player) {
        playerService.loginClientAsPlayer(client, player)
    }
}
