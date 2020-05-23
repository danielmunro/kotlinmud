package kotlinmud.player

import com.commit451.mailgun.Contact
import com.commit451.mailgun.SendMessageRequest
import java.io.File
import kotlinmud.fs.PLAYER_FILE
import kotlinmud.io.IOStatus
import kotlinmud.io.NIOClient
import kotlinmud.io.PreAuthRequest
import kotlinmud.io.PreAuthResponse
import kotlinmud.player.authStep.AuthStep
import kotlinmud.player.authStep.AuthStepService
import kotlinmud.player.authStep.EmailAuthStep
import kotlinmud.player.mapper.mapPlayer
import kotlinmud.player.model.MobCard
import kotlinmud.player.model.Player
import kotlinmud.player.model.PlayerBuilder
import kotlinmud.random.generateOTP
import kotlinmud.service.EmailService

class PlayerService(
    private val emailService: EmailService,
    private val players: MutableList<Player>,
    private val mobCards: MutableList<MobCard>
) {
    private val preAuthClients: MutableMap<NIOClient, AuthStep> = mutableMapOf()
    private val loggedInPlayers: MutableMap<NIOClient, Player> = mutableMapOf()

    fun handlePreAuthRequest(request: PreAuthRequest): PreAuthResponse {
        val authStep = preAuthClients[request.client] ?: EmailAuthStep(
            AuthStepService(
                this
            )
        )
        val response = authStep.handlePreAuthRequest(request)
        if (response.status == IOStatus.OK) {
            val nextAuthStep = authStep.getNextAuthStep()
            preAuthClients[request.client] = nextAuthStep
            request.client.write(nextAuthStep.promptMessage)
        }
        return response
    }

    fun findPlayerByOTP(otp: String): Player? {
        return players.find { it.lastOTP == otp }
    }

    fun createNewPlayerWithEmailAddress(emailAddress: String): Player {
        val player = PlayerBuilder()
            .email(emailAddress)
            .build()
        players.add(player)
        writePlayersFile()
        return player
    }

    fun sendOTP(player: Player) {
        val from = Contact("floodle@danmunro.com", "Floodle")
        val to = mutableListOf(Contact(player.email, "Login OTP"))
        val otp = generateOTP()
        emailService.sendEmail(
            SendMessageRequest.Builder(from)
                .to(to)
                .subject("Your OTP mud login")
                .text("Hi,\n\n Here is your OTP login: \"$otp\"\n\nIt will expire five minutes from now.")
                .build()
        )
        player.lastOTP = otp
    }

    fun loginClientAsPlayer(client: NIOClient, player: Player) {
        loggedInPlayers[client] = player
    }

    fun addPreAuthClient(client: NIOClient) {
        preAuthClients[client] = EmailAuthStep(AuthStepService(this))
    }

    fun writePlayersFile() {
        val file = File(PLAYER_FILE)
        file.writeText(players.joinToString { mapPlayer(it) })
    }
}
