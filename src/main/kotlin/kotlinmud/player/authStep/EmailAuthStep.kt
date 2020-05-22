package kotlinmud.player.authStep

import java.lang.IllegalStateException
import kotlinmud.io.IOStatus
import kotlinmud.io.PreAuthRequest
import kotlinmud.io.PreAuthResponse
import kotlinmud.player.AuthService

class EmailAuthStep(private val authService: AuthService) : AuthStep {
    override val authorizationStep: AuthorizationStep = AuthorizationStep.EMAIL
    override val promptMessage: String = "email address:"
    override val errorMessage: String = "sorry, try again."

    override fun handlePreAuthRequest(request: PreAuthRequest): PreAuthResponse {
        return try {
            authService.sendOTP(request.input)
            PreAuthResponse(request, IOStatus.OK, "ok")
        } catch (e: IllegalStateException) {
            PreAuthResponse(request, IOStatus.ERROR, e.message ?: "an error happened")
        }
    }

    override fun getNextAuthStep(): AuthStep {
        return PasswordAuthStep(authService)
    }
}
