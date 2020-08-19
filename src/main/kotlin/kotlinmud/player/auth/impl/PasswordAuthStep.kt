package kotlinmud.player.auth.impl

import kotlinmud.helper.logger
import kotlinmud.io.factory.createFailedPreAuthResponse
import kotlinmud.io.factory.createOkPreAuthResponse
import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.model.PreAuthResponse
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.auth.type.AuthStep
import kotlinmud.player.auth.type.AuthorizationStep
import kotlinmud.player.dao.PlayerDAO

class PasswordAuthStep(private val authService: AuthStepService, private val player: PlayerDAO) : AuthStep {
    override val authorizationStep = AuthorizationStep.PASSWORD
    override val promptMessage = "enter OTP:"
    override val errorMessage = "sorry, there was an error."
    private val logger = logger(this)

    override fun handlePreAuthRequest(request: PreAuthRequest): PreAuthResponse {
        return authService.findPlayerByOTP(request.input)?.let {
            validateOTPBelongsToPlayer(request, it.lastOTP)
        } ?: otpNotFound(request)
    }

    override fun getNextAuthStep(): AuthStep {
        return MobSelectAuthStep(authService, player)
    }

    private fun validateOTPBelongsToPlayer(request: PreAuthRequest, lastOTP: String?): PreAuthResponse {
        return if (lastOTP != null && lastOTP == player.lastOTP) {
            doLogin(request)
        } else {
            otpNotFound(request)
        }
    }

    private fun doLogin(request: PreAuthRequest): PreAuthResponse {
        logger.debug("success logging in :: {} as {}", request.client.socket.remoteAddress, player.email)
        authService.loginClientAsPlayer(request.client, player)
        return createOkPreAuthResponse(request, "Success.")
    }

    private fun otpNotFound(request: PreAuthRequest): PreAuthResponse {
        logger.debug("player with supplied OTP not found")
        return createFailedPreAuthResponse(request, "Player not found")
    }
}
