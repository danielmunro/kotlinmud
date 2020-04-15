package kotlinmud.player.authStep

import kotlinmud.io.PreAuthRequest
import kotlinmud.io.PreAuthResponse

class EmailAuthStep : AuthStep {
    override val authorizationStep: AuthorizationStep = AuthorizationStep.EMAIL
    override val promptMessage: String = "email address:"
    override val errorMessage: String = "sorry, try again."

    override fun handlePreAuthRequest(request: PreAuthRequest): PreAuthResponse {
        TODO("Not yet implemented")
    }

    override fun getNextAuthStep(): AuthStep {
        return PasswordAuthStep()
    }
}
