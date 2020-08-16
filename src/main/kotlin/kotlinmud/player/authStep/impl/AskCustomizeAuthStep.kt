package kotlinmud.player.authStep.impl

import kotlinmud.helper.string.matches
import kotlinmud.io.factory.createErrorPreAuthResponse
import kotlinmud.io.factory.createOkPreAuthResponse
import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.model.PreAuthResponse
import kotlinmud.player.authStep.service.AuthStepService
import kotlinmud.player.authStep.type.AuthStep
import kotlinmud.player.authStep.type.AuthorizationStep
import kotlinmud.player.dao.PlayerDAO

class AskCustomizeAuthStep(private val authStepService: AuthStepService, private val player: PlayerDAO) : AuthStep {
    override val authorizationStep = AuthorizationStep.ASK_CUSTOMIZE
    override val promptMessage = "Would you like to customize? (y/n)"
    override val errorMessage = "please answer 'yes' or 'no'"
    private var customize = false

    override fun handlePreAuthRequest(request: PreAuthRequest): PreAuthResponse {
        return if (request.input.matches("yes")) {
            customize = true
            createOkPreAuthResponse(request, "ok.")
        } else if (request.input.matches("no")) {
            customize = false
            createOkPreAuthResponse(request, "ok.")
        } else {
            createErrorPreAuthResponse(request, "not understood")
        }
    }

    override fun getNextAuthStep(): AuthStep {
        return if (customize) {
            CustomizeAuthStep(authStepService, player)
        } else {
            val funnel = authStepService.findCreationFunnelForEmail(player.email)!!
            val mob = funnel.build()
            CompleteAuthStep(mob.mobCard!!)
        }
    }
}
