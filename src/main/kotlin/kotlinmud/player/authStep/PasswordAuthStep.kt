package kotlinmud.player.authStep

import kotlinmud.io.IOStatus
import kotlinmud.io.PreAuthRequest
import kotlinmud.io.PreAuthResponse
import kotlinmud.player.AuthService
import org.slf4j.LoggerFactory

class PasswordAuthStep(private val authService: AuthService) : AuthStep {
    override val authorizationStep: AuthorizationStep = AuthorizationStep.PASSWORD
    override val promptMessage: String = "enter OTP:"
    override val errorMessage: String = "sorry, there was an error."
    private val logger = LoggerFactory.getLogger(PasswordAuthStep::class.java)

    override fun handlePreAuthRequest(request: PreAuthRequest): PreAuthResponse {
        return authService.findPlayerByOTP(request.input)?.let {
            logger.debug("success logging in :: {} as {}", request.client.socket.remoteAddress, it.email)
            authService.loginClientAsPlayer(request.client, it)
            PreAuthResponse(request, IOStatus.OK, "Success. Please do something")
        } ?: PreAuthResponse(request, IOStatus.FAILED, "Player not found")
    }

    override fun getNextAuthStep(): AuthStep {
        TODO("Not yet implemented")
    }
}
