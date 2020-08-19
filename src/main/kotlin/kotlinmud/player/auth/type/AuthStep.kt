package kotlinmud.player.auth.type

import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.type.IOStatus

interface AuthStep {
    val authorizationStep: AuthorizationStep
    val promptMessage: String
    val errorMessage: String
    fun handlePreAuthRequest(request: PreAuthRequest): IOStatus
    fun getNextAuthStep(): AuthStep
}
