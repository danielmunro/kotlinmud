package kotlinmud.player

import kotlinmud.io.NIOClient
import kotlinmud.service.PlayerService

class AuthService(private val playerService: PlayerService) {
    fun findPlayerByOTP(otp: String): Player? {
        return playerService.findPlayerByOTP(otp)
    }

    fun sendOTP(emailAddress: String) {
        playerService.findPlayerByEmailAddress(emailAddress)?.let {
            playerService.sendOTP(it)
        } ?: error("no player to send OTP to")
    }

    fun loginClientAsPlayer(client: NIOClient, player: Player) {
        playerService.loginClientAsPlayer(client, player)
    }
}
