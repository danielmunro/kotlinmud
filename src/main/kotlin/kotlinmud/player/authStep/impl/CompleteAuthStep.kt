package kotlinmud.player.authStep.impl

import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.model.PreAuthResponse
import kotlinmud.player.authStep.type.AuthStep
import kotlinmud.player.authStep.type.AuthorizationStep
import kotlinmud.player.dao.MobCardDAO

class CompleteAuthStep(val mobCard: MobCardDAO) : AuthStep {
    override val authorizationStep = AuthorizationStep.COMPLETE
    override val promptMessage = "you are logged in"
    override val errorMessage = ""

    override fun handlePreAuthRequest(request: PreAuthRequest): PreAuthResponse {
        TODO("Not yet implemented")
    }

    override fun getNextAuthStep(): AuthStep {
        TODO("Not yet implemented")
    }
}
