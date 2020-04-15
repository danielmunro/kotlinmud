package kotlinmud.player.authStep

import kotlinmud.io.PreAuthRequest
import kotlinmud.io.PreAuthResponse

interface AuthStep {
    val authorizationStep: AuthorizationStep
    val promptMessage: String
    val errorMessage: String
    fun handlePreAuthRequest(request: PreAuthRequest): PreAuthResponse
    fun getNextAuthStep(): AuthStep
}
