package kotlinmud.player.auth.impl

import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.type.IOStatus
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.auth.type.AuthStep
import kotlinmud.player.auth.type.AuthorizationStep
import kotlinmud.player.dao.PlayerDAO
import org.jetbrains.exposed.sql.transactions.transaction

class NewPasswordAuthStep(
    private val authService: AuthStepService,
    private val player: PlayerDAO,
) : AuthStep {
    override val authorizationStep = AuthorizationStep.NEW_PASSWORD
    override val promptMessage = "New account. Enter a password:"
    override val errorMessage = "Try a different password"

    override fun handlePreAuthRequest(request: PreAuthRequest): IOStatus {
        transaction { player.password = request.input }
        return IOStatus.OK
    }

    override fun getNextAuthStep(): AuthStep {
        return NewPasswordConfirmAuthStep(authService, player)
    }
}
