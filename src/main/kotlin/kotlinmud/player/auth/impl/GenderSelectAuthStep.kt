package kotlinmud.player.auth.impl

import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.type.IOStatus
import kotlinmud.mob.type.Gender
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.auth.type.AuthStep
import kotlinmud.player.auth.type.AuthorizationStep
import kotlinmud.player.dao.PlayerDAO

class GenderSelectAuthStep(
    private val authStepService: AuthStepService,
    private val player: PlayerDAO
) : AuthStep {
    override val authorizationStep = AuthorizationStep.GENDER_SELECT
    override val promptMessage = "gender (none/any/female/male):"
    override val errorMessage = "please choose from the list (default: none)"

    override fun handlePreAuthRequest(request: PreAuthRequest): IOStatus {
        authStepService.getCreationFunnelForEmail(player.email).gender = if ("none".startsWith(request.input)) {
            Gender.NONE
        } else if ("any".startsWith(request.input)) {
            Gender.ANY
        } else if ("female".startsWith(request.input)) {
            Gender.FEMALE
        } else if ("male".startsWith(request.input)) {
            Gender.MALE
        } else {
            return IOStatus.ERROR
        }

        return IOStatus.OK
    }

    override fun getNextAuthStep(): AuthStep {
        return AskCustomizeAuthStep(authStepService, player)
    }
}
