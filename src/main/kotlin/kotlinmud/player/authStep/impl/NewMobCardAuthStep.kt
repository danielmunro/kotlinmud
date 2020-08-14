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

class NewMobCardAuthStep(
    private val authService: AuthStepService,
    private val player: PlayerDAO
) : AuthStep {
    override val authorizationStep = AuthorizationStep.NEW_MOB
    override val promptMessage = "New mob. Is that right?"
    override val errorMessage = "Please answer yes or no (y/n):"
    private var proceed = false

    override fun handlePreAuthRequest(request: PreAuthRequest): PreAuthResponse {
        if (request.input.matches("yes")) {
            return createOkPreAuthResponse(request, "Ok.")
        } else if (request.input.matches("no")) {
            return createOkPreAuthResponse(request, "Ok.")
        }

        return createErrorPreAuthResponse(request, "Please respond with 'yes' or 'no'.")
    }

    override fun getNextAuthStep(): AuthStep {
        return if (proceed) {
            RaceSelectAuthStep()
        } else {
            MobSelectAuthStep(authService, player)
        }
    }
}
