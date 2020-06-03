package kotlinmud.player.authStep

import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.model.PreAuthResponse

interface AuthStep {
    val authorizationStep: AuthorizationStep
    val promptMessage: String
    val errorMessage: String
    fun handlePreAuthRequest(request: PreAuthRequest): PreAuthResponse
    fun getNextAuthStep(): AuthStep
}
