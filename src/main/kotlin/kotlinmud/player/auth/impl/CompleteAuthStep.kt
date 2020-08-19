package kotlinmud.player.auth.impl

import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.type.IOStatus
import kotlinmud.player.auth.type.AuthStep
import kotlinmud.player.auth.type.AuthorizationStep
import kotlinmud.player.dao.MobCardDAO

class CompleteAuthStep(val mobCard: MobCardDAO) : AuthStep {
    override val authorizationStep = AuthorizationStep.COMPLETE
    override val promptMessage = "you are logged in"
    override val errorMessage = ""

    override fun handlePreAuthRequest(request: PreAuthRequest): IOStatus {
        TODO("Not yet implemented")
    }

    override fun getNextAuthStep(): AuthStep {
        TODO("Not yet implemented")
    }
}
