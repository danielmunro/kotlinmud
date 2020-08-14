package kotlinmud.player.authStep.impl

import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.model.PreAuthResponse
import kotlinmud.player.authStep.type.AuthStep
import kotlinmud.player.authStep.type.AuthorizationStep

class RaceSelectAuthStep : AuthStep {
    override val authorizationStep = AuthorizationStep.RACE_SELECT
    override val promptMessage = "select a race:"
    override val errorMessage = "that is not a race. Enter 'help race' for help."

    override fun handlePreAuthRequest(request: PreAuthRequest): PreAuthResponse {
        TODO("Not yet implemented")
    }

    override fun getNextAuthStep(): AuthStep {
        return SpecializationAuthStep()
    }
}
