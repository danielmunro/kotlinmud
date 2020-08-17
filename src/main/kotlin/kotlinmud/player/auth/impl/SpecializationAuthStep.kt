package kotlinmud.player.auth.impl

import kotlinmud.helper.string.matches
import kotlinmud.io.factory.createErrorPreAuthResponse
import kotlinmud.io.factory.createOkPreAuthResponse
import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.model.PreAuthResponse
import kotlinmud.mob.specialization.helper.createSpecializationList
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.auth.type.AuthStep
import kotlinmud.player.auth.type.AuthorizationStep
import kotlinmud.player.dao.PlayerDAO

class SpecializationAuthStep(
    private val authStepService: AuthStepService,
    private val player: PlayerDAO
) : AuthStep {
    override val authorizationStep = AuthorizationStep.SPECIALIZATION_SELECT
    override val promptMessage = "select a specialization:"
    override val errorMessage = "that is not a specialization."
    private val specializations = createSpecializationList()

    override fun handlePreAuthRequest(request: PreAuthRequest): PreAuthResponse {
        return authStepService.findCreationFunnelForEmail(player.email)?.let { creation ->
            creation.specialization = specializations.find { request.input.matches(it.name) }!!
            createOkPreAuthResponse(request, "${creation.specialization} chosen.")
        } ?: createErrorPreAuthResponse(request, "that is not a valid specialization")
    }

    override fun getNextAuthStep(): AuthStep {
        return AskCustomizeAuthStep(authStepService, player)
    }
}
