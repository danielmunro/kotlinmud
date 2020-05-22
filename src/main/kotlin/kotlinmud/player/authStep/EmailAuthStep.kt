package kotlinmud.player.authStep

import kotlinmud.io.IOStatus
import kotlinmud.io.PreAuthRequest
import kotlinmud.io.PreAuthResponse

class EmailAuthStep(private val authService: AuthStepService) : AuthStep {
    override val authorizationStep: AuthorizationStep = AuthorizationStep.EMAIL
    override val promptMessage: String = "email address:"
    override val errorMessage: String = "sorry, try again."

    override fun handlePreAuthRequest(request: PreAuthRequest): PreAuthResponse {
        authService.findPlayerByOTP(request.input)?.let {
            authService.sendOTP(it)
        } ?: run {
            val player = authService.createPlayer(request.input)
            authService.sendOTP(player)
        }
        return PreAuthResponse(request, IOStatus.OK, "ok")
    }

    override fun getNextAuthStep(): AuthStep {
        return PasswordAuthStep(authService)
    }
}
