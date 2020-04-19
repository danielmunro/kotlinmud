package kotlinmud.service

import com.commit451.mailgun.Contact
import com.commit451.mailgun.SendMessageRequest
import kotlinmud.io.IOStatus
import kotlinmud.io.NIOClient
import kotlinmud.io.PreAuthRequest
import kotlinmud.io.PreAuthResponse
import kotlinmud.player.AuthService
import kotlinmud.player.Player
import kotlinmud.player.authStep.AuthStep
import kotlinmud.player.authStep.EmailAuthStep
import kotlinmud.random.generateOTP

class PlayerService(private val emailService: EmailService) {
    private val players: MutableList<Player> = mutableListOf()
    private val preAuthClients: Map<NIOClient, AuthStep> = mutableMapOf()
    private val loggedInPlayers: Map<NIOClient, Player> = mutableMapOf()

    fun handlePreAuthRequest(request: PreAuthRequest): PreAuthResponse {
        val authStep = preAuthClients[request.client] ?: EmailAuthStep(AuthService(this))
        val response = authStep.handlePreAuthRequest(request)
        if (response.status == IOStatus.OK) {
            preAuthClients.plus(Pair(request.client, authStep.getNextAuthStep()))
        }
        return response
    }

    fun findPlayerByEmailAddress(emailAddress: String): Player? {
        return players.find { it.email == emailAddress }
    }

    fun findPlayerByOTP(otp: String): Player? {
        return players.find { it.lastOTP == otp }
    }

    fun sendOTP(player: Player) {
        val from = Contact("floodle@danmunro.com", "Floodle")
        val to = mutableListOf(Contact(player.email, "Login OTP"))
        val otp = generateOTP()
        emailService.sendEmail(
            SendMessageRequest.Builder(from)
                .to(to)
                .subject("Your OTP mud login")
                .text("Hi,\n\n Here is your OTP login: $otp\n\nIt will expire five minutes from now.")
                .build()
        )
        player.lastOTP = otp
    }

    fun loginClientAsPlayer(client: NIOClient, player: Player) {
        loggedInPlayers.plus(Pair(client, player))
    }

    fun addPreAuthClient(client: NIOClient) {
        preAuthClients.plus(Pair(client, EmailAuthStep(AuthService(this))))
    }
}
