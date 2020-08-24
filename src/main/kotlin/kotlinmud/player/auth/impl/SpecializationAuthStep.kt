package kotlinmud.player.auth.impl

import kotlinmud.helper.string.matches
import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.type.IOStatus
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

    override fun handlePreAuthRequest(request: PreAuthRequest): IOStatus {
        val specialization = specializations.find { spec -> request.input.matches(spec.name) } ?: return IOStatus.ERROR

        authStepService.getCreationFunnelForEmail(player.email).specialization = specialization

        return IOStatus.OK
    }

    override fun getNextAuthStep(): AuthStep {
        return AskCustomizeAuthStep(authStepService, player)
    }
}
