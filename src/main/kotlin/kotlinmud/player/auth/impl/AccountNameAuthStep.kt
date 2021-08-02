package kotlinmud.player.auth.impl

import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.type.IOStatus
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.auth.type.AuthStep
import kotlinmud.player.auth.type.AuthorizationStep
import kotlinmud.player.dao.PlayerDAO

class AccountNameAuthStep(private val authService: AuthStepService) : AuthStep {
    override val authorizationStep = AuthorizationStep.ACCOUNT_NAME
    override val promptMessage = "account name:"
    override val errorMessage = "that account name is missing or invalid."
    private var player: PlayerDAO? = null
    private var isNewPlayer = false

    override fun handlePreAuthRequest(request: PreAuthRequest): IOStatus {
        player = authService.findPlayerByName(request.input)
        if (player == null) {
            player = authService.createPlayer(request.input)
            isNewPlayer = true
        }
        return IOStatus.OK
    }

    override fun getNextAuthStep(): AuthStep {
        if (isNewPlayer) {
            return NewPasswordAuthStep(authService, player!!)
        }
        return player?.let {
            PasswordAuthStep(authService, it)
        } ?: AccountNameAuthStep(authService)
    }
}
