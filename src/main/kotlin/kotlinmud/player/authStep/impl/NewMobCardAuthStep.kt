package kotlinmud.player.authStep.impl

import kotlinmud.helper.string.matches
import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.model.PreAuthResponse
import kotlinmud.io.type.IOStatus
import kotlinmud.player.authStep.service.AuthStepService
import kotlinmud.player.authStep.type.AuthStep
import kotlinmud.player.authStep.type.AuthorizationStep
import kotlinmud.player.dao.PlayerDAO

class NewMobCardAuthStep(
    private val authService: AuthStepService,
    private val player: PlayerDAO
) : AuthStep {
    override val authorizationStep = AuthorizationStep.NEW_MOB
    override val promptMessage = "New mob. Is that right?"
    override val errorMessage = "Please answer yes or no (y/n):"
    private var proceed = false

    override fun handlePreAuthRequest(request: PreAuthRequest): PreAuthResponse {
        if (matches("yes", request.input)) {
            return PreAuthResponse(request, IOStatus.OK, "Ok.")
        } else if (matches("no", request.input)) {
            return PreAuthResponse(request, IOStatus.OK, "Ok.")
        }

        return PreAuthResponse(request, IOStatus.ERROR, "Please respond with 'yes' or 'no'.")
    }

    override fun getNextAuthStep(): AuthStep {
        return if (proceed) {
            RaceSelectAuthStep()
        } else {
            MobSelectAuthStep(authService, player)
        }
    }
}
