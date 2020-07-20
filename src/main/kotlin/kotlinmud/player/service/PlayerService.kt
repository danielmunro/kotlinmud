package kotlinmud.player.service

import com.commit451.mailgun.Contact
import com.commit451.mailgun.SendMessageRequest
import kotlinmud.event.impl.Event
import kotlinmud.event.impl.PlayerLoggedInEvent
import kotlinmud.event.service.EventService
import kotlinmud.event.type.EventType
import kotlinmud.helper.logger
import kotlinmud.helper.random.generateOTP
import kotlinmud.io.model.Client
import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.model.PreAuthResponse
import kotlinmud.io.type.IOStatus
import kotlinmud.mob.table.Mobs
import kotlinmud.player.authStep.AuthStep
import kotlinmud.player.authStep.AuthStepService
import kotlinmud.player.authStep.impl.CompleteAuthStep
import kotlinmud.player.authStep.impl.EmailAuthStep
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.player.dao.PlayerDAO
import kotlinmud.player.table.MobCards
import kotlinmud.player.table.Players
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class PlayerService(
    private val emailService: EmailService,
    private val eventService: EventService
) {
    private val preAuthClients: MutableMap<Client, AuthStep> = mutableMapOf()
    private val loggedInPlayers: MutableMap<Client, PlayerDAO> = mutableMapOf()
    private val logger = logger(this)

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

    fun findMobCardByName(name: String): MobCardDAO? {
        return transaction {
            (Mobs innerJoin MobCards).select {
                Mobs.mobCardId eq MobCards.id and (Mobs.name eq name)
            }.firstOrNull()?.let {
                MobCardDAO.wrapRow(it)
            }
        }
    }

    fun findPlayerByOTP(otp: String): PlayerDAO? {
        return Players.select { Players.lastOTP eq otp }.firstOrNull()?.let {
            PlayerDAO.wrapRow(it)
        }
    }

    fun createNewPlayerWithEmailAddress(emailAddress: String): PlayerDAO {
        return transaction {
            PlayerDAO.new {
                email = emailAddress
            }
        }
    }

    fun sendOTP(player: PlayerDAO) {
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

    fun loginClientAsPlayer(client: Client, player: PlayerDAO) {
        loggedInPlayers[client] = player
    }

    fun addPreAuthClient(client: Client) {
        preAuthClients[client] = EmailAuthStep(AuthStepService(this))
    }

    private fun loginMob(client: Client, mobCard: MobCardDAO) {
        eventService.publish(
            Event(
                EventType.CLIENT_LOGGED_IN,
                PlayerLoggedInEvent(client, mobCard)
            )
        )
    }
}
