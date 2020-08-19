package kotlinmud.player.auth.impl

import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.type.IOStatus
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.auth.type.AuthStep
import kotlinmud.player.auth.type.AuthorizationStep
import kotlinmud.player.dao.PlayerDAO

class EmailAuthStep(private val authService: AuthStepService) : AuthStep {
    override val authorizationStep = AuthorizationStep.EMAIL
    override val promptMessage = "email address:"
    override val errorMessage = "sorry, try again."
    private lateinit var player: PlayerDAO

    override fun handlePreAuthRequest(request: PreAuthRequest): IOStatus {
        player = authService.findPlayerByEmail(request.input)
            ?.also(::sendOTP)
            ?: createPlayer(request.input)

        return IOStatus.OK
    }

    override fun getNextAuthStep(): AuthStep {
        return PasswordAuthStep(authService, player)
    }

    private fun createPlayer(input: String): PlayerDAO {
        return authService.createPlayer(input).also(::sendOTP)
    }

    private fun sendOTP(player: PlayerDAO) {
        authService.sendOTP(player)
    }
}
