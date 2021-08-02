package kotlinmud.player.auth.impl

import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.type.IOStatus
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.auth.type.AuthStep
import kotlinmud.player.auth.type.AuthorizationStep
import kotlinmud.player.dao.PlayerDAO

class NewPasswordConfirmAuthStep(
    private val authService: AuthStepService,
    private val player: PlayerDAO,
) : AuthStep {
    override val authorizationStep = AuthorizationStep.NEW_PASSWORD_CONFIRM
    override val promptMessage = "Confirm:"
    override val errorMessage = "Try again"
    private var confirmed = false

    override fun handlePreAuthRequest(request: PreAuthRequest): IOStatus {
        if (request.input != player.password) {
            return IOStatus.OK
        }

        confirmed = true

        return IOStatus.OK
    }

    override fun getNextAuthStep(): AuthStep {
        if (confirmed) {
            return MobSelectAuthStep(authService, player)
        }

        return NewPasswordAuthStep(authService, player)
    }
}
