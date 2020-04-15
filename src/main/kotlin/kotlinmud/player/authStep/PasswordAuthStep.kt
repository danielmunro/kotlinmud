package kotlinmud.player.authStep

import kotlinmud.io.PreAuthRequest
import kotlinmud.io.PreAuthResponse

class PasswordAuthStep : AuthStep {
    override val authorizationStep: AuthorizationStep = AuthorizationStep.PASSWORD
    override val promptMessage: String = "enter OTP:"
    override val errorMessage: String = "sorry, there was an error."

    override fun handlePreAuthRequest(request: PreAuthRequest): PreAuthResponse {
        TODO("Not yet implemented")
    }

    override fun getNextAuthStep(): AuthStep {
        TODO("Not yet implemented")
    }
}
