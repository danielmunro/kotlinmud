package kotlinmud.player.service

import com.commit451.mailgun.Contact
import com.commit451.mailgun.SendMessageRequest
import io.konform.validation.Invalid
import io.konform.validation.Validation
import io.konform.validation.jsonschema.pattern
import kotlinmud.event.factory.createClientLoggedInEvent
import kotlinmud.event.service.EventService
import kotlinmud.helper.logger
import kotlinmud.helper.random.generateOTP
import kotlinmud.helper.string.matches
import kotlinmud.io.model.Client
import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.model.PreAuthResponse
import kotlinmud.io.type.IOStatus
import kotlinmud.mob.model.Mob
import kotlinmud.mob.model.PlayerMob
import kotlinmud.mob.service.MobService
import kotlinmud.player.auth.impl.CompleteAuthStep
import kotlinmud.player.auth.impl.EmailAuthStep
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.auth.type.AuthStep
import kotlinmud.player.dao.PlayerDAO
import kotlinmud.player.exception.EmailFormatException
import org.jetbrains.exposed.sql.transactions.transaction
import kotlinmud.player.repository.findPlayerByOTP as findPlayerByOTPQuery

class PlayerService(
    private val emailService: EmailService,
    private val eventService: EventService,
    private val mobService: MobService,
) {
    private val preAuthClients: MutableMap<Client, AuthStep> = mutableMapOf()
    private val loggedInPlayers: MutableMap<Int, PlayerDAO> = mutableMapOf()
    private val loggedInMobs: MutableMap<PlayerDAO, PlayerMob> = mutableMapOf()
    private lateinit var authStepService: AuthStepService
    private val logger = logger(this)

    fun setAuthStep(client: Client, authStep: AuthStep) {
        preAuthClients[client] = authStep
    }

    fun setAuthStepService(authStepService: AuthStepService) {
        this.authStepService = authStepService
    }

    suspend fun handlePreAuthRequest(request: PreAuthRequest): PreAuthResponse {
        val authStep = preAuthClients[request.client] ?: EmailAuthStep(authStepService)
        val ioStatus = authStep.handlePreAuthRequest(request)
        logger.debug("pre-auth request :: {}, {}, {}", authStep.authorizationStep, request.input, ioStatus)
        val nextAuthStep = if (ioStatus == IOStatus.OK) {
            proceedAuth(request, authStep)
        } else authStep
        return PreAuthResponse(
            request,
            ioStatus,
            if (ioStatus == IOStatus.OK) "ok." else authStep.errorMessage,
            nextAuthStep
        )
    }

    fun findPlayerMobByName(name: String): PlayerMob? {
        return mobService.findPlayerMobByName(name)
    }

    fun findLoggedInPlayerMobByName(name: String): Mob? {
        return loggedInMobs.values.find { name.matches(it.name) }
    }

    fun findPlayerByOTP(otp: String): PlayerDAO? {
        return findPlayerByOTPQuery(otp)
    }

    fun createNewPlayerWithEmailAddress(emailAddress: String): PlayerDAO {
        validateEmailAddressFormat(emailAddress)
        return transaction {
            PlayerDAO.new {
                email = emailAddress
                name = "foo"
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
        transaction { player.lastOTP = otp }
    }

    fun loginClientAsPlayer(client: Client, player: PlayerDAO) {
        loggedInPlayers[client.id] = player
    }

    fun loginPlayerAsMob(player: PlayerDAO, mob: PlayerMob) {
        loggedInMobs[player] = mob
    }

    fun addPreAuthClient(client: Client) {
        preAuthClients[client] = EmailAuthStep(authStepService)
    }

    fun getAuthStepForClient(client: Client): AuthStep? {
        return preAuthClients[client]
    }

    private suspend fun proceedAuth(request: PreAuthRequest, authStep: AuthStep): AuthStep {
        val nextAuthStep = authStep.getNextAuthStep()
        if (nextAuthStep is CompleteAuthStep) {
            loginMob(request.client, nextAuthStep.playerMob)
        }
        preAuthClients[request.client] = nextAuthStep
        request.client.write(nextAuthStep.promptMessage)
        return nextAuthStep
    }

    private fun validateEmailAddressFormat(emailAddress: String) {
        val validateEmail = Validation<String> {
            pattern(".+@.+..+")
        }
        val result = validateEmail(emailAddress)
        if (result is Invalid) {
            throw EmailFormatException()
        }
    }

    private suspend fun loginMob(client: Client, mob: PlayerMob) {
        val player = loggedInPlayers[client.id]!!
        loginPlayerAsMob(player, mob)
        eventService.publish(createClientLoggedInEvent(client, mob))
    }
}
