package kotlinmud.player.authStep.impl

import kotlinmud.helper.logger
import kotlinmud.io.factory.createFailedPreAuthResponse
import kotlinmud.io.factory.createOkPreAuthResponse
import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.model.PreAuthResponse
import kotlinmud.player.authStep.service.AuthStepService
import kotlinmud.player.authStep.type.AuthStep
import kotlinmud.player.authStep.type.AuthorizationStep
import kotlinmud.player.dao.PlayerDAO

class PasswordAuthStep(private val authService: AuthStepService) : AuthStep {
    override val authorizationStep = AuthorizationStep.PASSWORD
    override val promptMessage = "enter OTP:"
    override val errorMessage = "sorry, there was an error."
    private val logger = logger(this)
    private lateinit var player: PlayerDAO

    override fun handlePreAuthRequest(request: PreAuthRequest): PreAuthResponse {
        return authService.findPlayerByOTP(request.input)?.let {
            doLogin(request, it)
        } ?: otpNotFound(request)
    }

    override fun getNextAuthStep(): AuthStep {
        return MobSelectAuthStep(authService, player)
    }

    private fun doLogin(request: PreAuthRequest, player: PlayerDAO): PreAuthResponse {
        this.player = player
        logger.debug("success logging in :: {} as {}", request.client.socket.remoteAddress, player.email)
        authService.loginClientAsPlayer(request.client, player)
        return createOkPreAuthResponse(request, "Success. Please do something")
    }

    private fun otpNotFound(request: PreAuthRequest): PreAuthResponse {
        logger.debug("player with supplied OTP not found")
        return createFailedPreAuthResponse(request, "Player not found")
    }
}
