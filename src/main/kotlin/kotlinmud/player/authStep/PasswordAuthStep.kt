package kotlinmud.player.authStep

import kotlinmud.io.IOStatus
import kotlinmud.io.PreAuthRequest
import kotlinmud.io.PreAuthResponse
import kotlinmud.player.AuthService

class PasswordAuthStep(private val authService: AuthService) : AuthStep {
    override val authorizationStep: AuthorizationStep = AuthorizationStep.PASSWORD
    override val promptMessage: String = "enter OTP:"
    override val errorMessage: String = "sorry, there was an error."

    override fun handlePreAuthRequest(request: PreAuthRequest): PreAuthResponse {
        return authService.findPlayerByOTP(request.input)?.let {
            authService.loginClientAsPlayer(request.client, it)
            PreAuthResponse(request, IOStatus.OK, "Success. Please do something")
        } ?: PreAuthResponse(request, IOStatus.FAILED, "Player not found")
    }

    override fun getNextAuthStep(): AuthStep {
        TODO("Not yet implemented")
    }
}
