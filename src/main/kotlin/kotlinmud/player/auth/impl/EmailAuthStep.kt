package kotlinmud.player.auth.impl

import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.type.IOStatus
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.auth.type.AuthStep
import kotlinmud.player.auth.type.AuthorizationStep
import kotlinmud.player.dao.PlayerDAO
import kotlinmud.player.exception.EmailFormatException

class EmailAuthStep(private val authService: AuthStepService) : AuthStep {
    override val authorizationStep = AuthorizationStep.EMAIL
    override val promptMessage = "email address:"
    override val errorMessage = "sorry, try again."
    private lateinit var player: PlayerDAO

    override fun handlePreAuthRequest(request: PreAuthRequest): IOStatus {
        return try {
            player = authService.findPlayerByEmail(request.input)
                ?.also(::sendOTP)
                ?: createPlayer(request.input)

            IOStatus.OK
        } catch (e: EmailFormatException) {
            IOStatus.ERROR
        }
    }

    override fun getNextAuthStep(): AuthStep {
        return OTPAuthStep(authService, player)
    }

    private fun createPlayer(input: String): PlayerDAO {
        return authService.createPlayer(input)
    }

    private fun sendOTP(player: PlayerDAO) {
        authService.sendOTP(player)
    }
}
