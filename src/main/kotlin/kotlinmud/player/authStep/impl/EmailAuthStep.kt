package kotlinmud.player.authStep.impl

import kotlinmud.io.factory.createOkPreAuthResponse
import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.model.PreAuthResponse
import kotlinmud.player.authStep.service.AuthStepService
import kotlinmud.player.authStep.type.AuthStep
import kotlinmud.player.authStep.type.AuthorizationStep

class EmailAuthStep(private val authService: AuthStepService) : AuthStep {
    override val authorizationStep = AuthorizationStep.EMAIL
    override val promptMessage = "email address:"
    override val errorMessage = "sorry, try again."

    override fun handlePreAuthRequest(request: PreAuthRequest): PreAuthResponse {
        authService.findPlayerByOTP(request.input)?.let {
            authService.sendOTP(it)
        } ?: createPlayer(request.input)

        return createOkPreAuthResponse(request, "ok")
    }

    override fun getNextAuthStep(): AuthStep {
        return PasswordAuthStep(authService)
    }

    private fun createPlayer(input: String) {
        authService.sendOTP(authService.createPlayer(input))
    }
}
