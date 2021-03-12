package kotlinmud.player.auth.impl

import kotlinmud.helper.string.matches
import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.type.IOStatus
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.auth.type.AuthStep
import kotlinmud.player.auth.type.AuthorizationStep
import kotlinmud.player.dao.PlayerDAO

class NewPlayerMobConfirmAuthStep(
    private val authService: AuthStepService,
    private val player: PlayerDAO,
    private val name: String
) : AuthStep {
    override val authorizationStep = AuthorizationStep.NEW_MOB
    override val promptMessage = "New mob. Is that right?"
    override val errorMessage = "Please answer yes or no (y/n):"
    private var proceed = false

    override fun handlePreAuthRequest(request: PreAuthRequest): IOStatus {
        if (request.input.matches("yes")) {
            val creationFunnel = authService.createCreationFunnel(player.email)
            creationFunnel.mobName = name
            authService.addCreationFunnel(creationFunnel)
            proceed = true
            return IOStatus.OK
        } else if (request.input.matches("no")) {
            return IOStatus.OK
        }

        return IOStatus.ERROR
    }

    override fun getNextAuthStep(): AuthStep {
        return if (proceed) {
            RaceSelectAuthStep(authService, player)
        } else {
            MobSelectAuthStep(authService, player)
        }
    }
}
