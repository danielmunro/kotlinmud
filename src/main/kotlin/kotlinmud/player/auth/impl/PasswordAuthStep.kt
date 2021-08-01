package kotlinmud.player.auth.impl

import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.type.IOStatus
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.auth.type.AuthStep
import kotlinmud.player.auth.type.AuthorizationStep
import kotlinmud.player.dao.PlayerDAO

class PasswordAuthStep(
    private val authStepService: AuthStepService,
    private var player: PlayerDAO,
) : AuthStep {
    override val authorizationStep = AuthorizationStep.PASSWORD
    override val promptMessage = "password:"
    override val errorMessage = "password invalid"

    override fun handlePreAuthRequest(request: PreAuthRequest): IOStatus {
        return if (player.password == request.input) {
            authStepService.loginClientAsPlayer(request.client, player)
            IOStatus.OK
        } else IOStatus.FAILED
    }

    override fun getNextAuthStep(): AuthStep {
        return MobSelectAuthStep(authStepService, player)
    }
}
