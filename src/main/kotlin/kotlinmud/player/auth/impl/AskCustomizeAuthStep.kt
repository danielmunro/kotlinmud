package kotlinmud.player.auth.impl

import kotlinmud.helper.string.matches
import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.type.IOStatus
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.auth.type.AuthStep
import kotlinmud.player.auth.type.AuthorizationStep
import kotlinmud.player.dao.PlayerDAO

class AskCustomizeAuthStep(private val authStepService: AuthStepService, private val player: PlayerDAO) : AuthStep {
    override val authorizationStep = AuthorizationStep.ASK_CUSTOMIZE
    override val promptMessage = "Would you like to customize? (y/n)"
    override val errorMessage = "please answer 'yes' or 'no'"
    private var customize = false

    override fun handlePreAuthRequest(request: PreAuthRequest): IOStatus {
        return if (request.input.matches("yes")) {
            customize = true
            IOStatus.OK
        } else if (request.input.matches("no")) {
            customize = false
            IOStatus.OK
        } else {
            IOStatus.ERROR
        }
    }

    override fun getNextAuthStep(): AuthStep {
        return if (customize) {
            CustomizationAuthStep(authStepService, player)
        } else {
            val funnel = authStepService.getCreationFunnelForName(player.name)
            val mob = funnel.build(player)
            CompleteAuthStep(mob)
        }
    }
}
