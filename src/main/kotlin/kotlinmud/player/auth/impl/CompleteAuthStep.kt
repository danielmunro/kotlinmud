package kotlinmud.player.auth.impl

import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.type.IOStatus
import kotlinmud.mob.model.PlayerMob
import kotlinmud.player.auth.type.AuthStep
import kotlinmud.player.auth.type.AuthorizationStep

class CompleteAuthStep(val playerMob: PlayerMob) : AuthStep {
    override val authorizationStep = AuthorizationStep.COMPLETE
    override val promptMessage = ""
    override val errorMessage = ""

    override fun handlePreAuthRequest(request: PreAuthRequest): IOStatus {
        return IOStatus.OK
    }

    override fun getNextAuthStep(): AuthStep {
        TODO("Not yet implemented")
    }
}
