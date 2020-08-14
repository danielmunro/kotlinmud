package kotlinmud.player.authStep.impl

import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.model.PreAuthResponse
import kotlinmud.player.authStep.type.AuthStep
import kotlinmud.player.authStep.type.AuthorizationStep

class SpecializationAuthStep : AuthStep {
    override val authorizationStep = AuthorizationStep.SPECIALIZATION_SELECT
    override val promptMessage = "select a specialization:"
    override val errorMessage = "that is not a specialization."

    override fun handlePreAuthRequest(request: PreAuthRequest): PreAuthResponse {
        TODO("Not yet implemented")
    }

    override fun getNextAuthStep(): AuthStep {
        TODO("Not yet implemented")
    }
}
