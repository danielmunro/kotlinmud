package kotlinmud.player

import kotlinmud.io.NIOClient
import kotlinmud.player.model.Player
import kotlinmud.player.model.PlayerBuilder

class AuthService(private val playerService: PlayerService) {
    fun findPlayerByOTP(otp: String): Player? {
        return playerService.findPlayerByOTP(otp)
    }

    fun sendOTP(emailAddress: String) {
        playerService.findPlayerByEmailAddress(emailAddress)?.let {
            playerService.sendOTP(it)
        } ?: run {
            val player = PlayerBuilder()
                .email(emailAddress)
                .build()
            playerService.addPlayer(player)
            playerService.sendOTP(player)
        }
    }

    fun loginClientAsPlayer(client: NIOClient, player: Player) {
        playerService.loginClientAsPlayer(client, player)
    }
}
