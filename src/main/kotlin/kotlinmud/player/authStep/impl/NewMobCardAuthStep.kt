package kotlinmud.player.authStep.impl

import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.model.PreAuthResponse
import kotlinmud.player.authStep.AuthStep
import kotlinmud.player.authStep.AuthorizationStep

class NewMobCardAuthStep : AuthStep {
    override val authorizationStep: AuthorizationStep
        get() = TODO("Not yet implemented")
    override val promptMessage: String
        get() = TODO("Not yet implemented")
    override val errorMessage: String
        get() = TODO("Not yet implemented")

    override fun handlePreAuthRequest(request: PreAuthRequest): PreAuthResponse {
        TODO("Not yet implemented")
    }

    override fun getNextAuthStep(): AuthStep {
        TODO("Not yet implemented")
    }
}
