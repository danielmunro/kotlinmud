package kotlinmud.player.authStep.impl

import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.model.PreAuthResponse
import kotlinmud.io.type.IOStatus
import kotlinmud.player.authStep.AuthStep
import kotlinmud.player.authStep.AuthStepService
import kotlinmud.player.authStep.AuthorizationStep
import kotlinmud.player.dao.PlayerDAO
import org.slf4j.LoggerFactory

class PasswordAuthStep(private val authService: AuthStepService) :
    AuthStep {
    override val authorizationStep: AuthorizationStep =
        AuthorizationStep.PASSWORD
    override val promptMessage: String = "enter OTP:"
    override val errorMessage: String = "sorry, there was an error."
    private val logger = LoggerFactory.getLogger(PasswordAuthStep::class.java)
    private var player: PlayerDAO? = null

    override fun handlePreAuthRequest(request: PreAuthRequest): PreAuthResponse {
        return authService.findPlayerByOTP(request.input)?.let {
            player = it
            logger.debug("success logging in :: {} as {}", request.client.socket.remoteAddress, it.email)
            authService.loginClientAsPlayer(request.client, it)
            PreAuthResponse(request, IOStatus.OK, "Success. Please do something")
        } ?: run {
            logger.debug("player with supplied OTP not found")
            PreAuthResponse(request, IOStatus.FAILED, "Player not found")
        }
    }

    override fun getNextAuthStep(): AuthStep {
        return MobSelectAuthStep(authService, player!!)
    }
}
