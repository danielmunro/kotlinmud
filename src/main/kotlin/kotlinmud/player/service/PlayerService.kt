package kotlinmud.player.service

import com.commit451.mailgun.Contact
import com.commit451.mailgun.SendMessageRequest
import java.io.File
import kotlinmud.event.impl.Event
import kotlinmud.event.impl.PlayerLoggedInEvent
import kotlinmud.event.service.EventService
import kotlinmud.event.type.EventType
import kotlinmud.fs.MOB_CARD_FILE
import kotlinmud.fs.PLAYER_FILE
import kotlinmud.helper.logger
import kotlinmud.io.model.Client
import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.model.PreAuthResponse
import kotlinmud.io.type.IOStatus
import kotlinmud.player.authStep.AuthStep
import kotlinmud.player.authStep.AuthStepService
import kotlinmud.player.authStep.impl.CompleteAuthStep
import kotlinmud.player.authStep.impl.EmailAuthStep
import kotlinmud.player.mapper.mapMobCard
import kotlinmud.player.mapper.mapPlayer
import kotlinmud.player.model.MobCard
import kotlinmud.player.model.Player
import kotlinmud.player.model.PlayerBuilder
import kotlinmud.random.generateOTP

class PlayerService(
    private val emailService: EmailService,
    private val players: MutableList<Player>,
    private val mobCards: MutableList<MobCard>,
    private val eventService: EventService
) {
    private val preAuthClients: MutableMap<Client, AuthStep> = mutableMapOf()
    private val loggedInPlayers: MutableMap<Client, Player> = mutableMapOf()
    private val logger = logger(this)

    init {
        logger.debug("player service with {} players and {} mob cards", players.size, mobCards.size)
    }

    fun handlePreAuthRequest(request: PreAuthRequest): PreAuthResponse {
        val authStep = preAuthClients[request.client] ?: EmailAuthStep(
            AuthStepService(this)
        )
        val response = authStep.handlePreAuthRequest(request)
        logger.debug("pre-auth request :: {}, {}, {}", authStep.authorizationStep, request.input, response.status)
        if (response.status == IOStatus.OK) {
            val nextAuthStep = authStep.getNextAuthStep()
            if (nextAuthStep is CompleteAuthStep) {
                loginMob(request.client, nextAuthStep.mobCard)
            }
            preAuthClients[request.client] = nextAuthStep
            request.client.write(nextAuthStep.promptMessage)
        }
        return response
    }

    fun findMobCardByName(name: String): MobCard? {
        return mobCards.find { it.mobName == name }
    }

    fun findPlayerByOTP(otp: String): Player? {
        return players.find { it.lastOTP == otp }
    }

    fun createNewPlayerWithEmailAddress(emailAddress: String): Player {
        val player = PlayerBuilder()
            .email(emailAddress)
            .build()
        players.add(player)
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

    fun getMobCards(): List<MobCard> {
        return mobCards
    }

    fun loginClientAsPlayer(client: Client, player: Player) {
        loggedInPlayers[client] = player
    }

    fun addPreAuthClient(client: Client) {
        preAuthClients[client] = EmailAuthStep(AuthStepService(this))
    }

    fun addMobCard(mobCard: MobCard) {
        mobCards.add(mobCard)
    }

    fun persist() {
        logger.debug("player service persist :: {} players, {} mob cards", players.size, mobCards.size)
        writePlayersFile()
        writeMobCardsFile()
    }

    private fun writePlayersFile() {
        val file = File(PLAYER_FILE)
        file.writeText(players.joinToString("\n") { mapPlayer(it) })
    }

    private fun writeMobCardsFile() {
        val file = File(MOB_CARD_FILE)
        file.writeText(mobCards.joinToString("\n") { mapMobCard(it) })
    }

    private fun loginMob(client: Client, mobCard: MobCard) {
        eventService.publish(
            Event(
                EventType.CLIENT_LOGGED_IN,
                PlayerLoggedInEvent(client, mobCard)
            )
        )
    }
}
