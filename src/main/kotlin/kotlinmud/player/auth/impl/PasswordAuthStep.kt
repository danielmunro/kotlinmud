package kotlinmud.player.auth.impl

import kotlinmud.helper.logger
import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.type.IOStatus
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.auth.type.AuthStep
import kotlinmud.player.auth.type.AuthorizationStep
import kotlinmud.player.dao.PlayerDAO

class PasswordAuthStep(private val authService: AuthStepService, private val player: PlayerDAO) : AuthStep {
    override val authorizationStep = AuthorizationStep.PASSWORD
    override val promptMessage = "enter OTP:"
    override val errorMessage = "sorry, there was an error."
    private val logger = logger(this)

    override fun handlePreAuthRequest(request: PreAuthRequest): IOStatus {
        return authService.findPlayerByOTP(request.input)?.let {
            validateOTPBelongsToPlayer(request, it.lastOTP!!)
        } ?: otpNotFound()
    }

    override fun getNextAuthStep(): AuthStep {
        return MobSelectAuthStep(authService, player)
    }

    private fun validateOTPBelongsToPlayer(request: PreAuthRequest, lastOTP: String): IOStatus? {
        return if (lastOTP == player.lastOTP) {
            doLogin(request)
        } else null
    }

    private fun doLogin(request: PreAuthRequest): IOStatus {
        logger.debug("success logging in :: {} as {}", request.client.socket.remoteAddress, player.email)
        authService.loginClientAsPlayer(request.client, player)
        return IOStatus.OK
    }

    private fun otpNotFound(): IOStatus {
        logger.debug("player with supplied OTP not found")
        return IOStatus.ERROR
    }
}
