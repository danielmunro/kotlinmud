package kotlinmud.player.auth.impl

import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.model.PreAuthResponse
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.auth.type.AuthStep
import kotlinmud.player.auth.type.AuthorizationStep
import kotlinmud.player.dao.PlayerDAO

class CustomizeAuthStep(private val authStepService: AuthStepService, private val player: PlayerDAO) : AuthStep {
    override val authorizationStep = AuthorizationStep.CUSTOMIZE
    override val promptMessage = "What would you like to do (add/remove/list/help/done)?"
    override val errorMessage = "What was that?"
    private var done = false

    override fun handlePreAuthRequest(request: PreAuthRequest): PreAuthResponse {
        TODO("Not yet implemented")
    }

    override fun getNextAuthStep(): AuthStep {
        return if (done) {
            val funnel = authStepService.findCreationFunnelForEmail(player.email)!!
            val mob = funnel.build()
            CompleteAuthStep(mob.mobCard!!)
        } else {
            this
        }
    }
}
