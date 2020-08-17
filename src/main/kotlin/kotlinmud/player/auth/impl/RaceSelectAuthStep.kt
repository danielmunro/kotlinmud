package kotlinmud.player.auth.impl

import kotlinmud.io.factory.createErrorPreAuthResponse
import kotlinmud.io.factory.createOkPreAuthResponse
import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.model.PreAuthResponse
import kotlinmud.mob.race.factory.matchRace
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.auth.type.AuthStep
import kotlinmud.player.auth.type.AuthorizationStep
import kotlinmud.player.dao.PlayerDAO

class RaceSelectAuthStep(private val authStepService: AuthStepService, private val player: PlayerDAO) : AuthStep {
    override val authorizationStep = AuthorizationStep.RACE_SELECT
    override val promptMessage = "select a race:"
    override val errorMessage = "that is not a race. Enter 'help race' for help."

    override fun handlePreAuthRequest(request: PreAuthRequest): PreAuthResponse {
        return matchRace(request.input)?.let { race ->
            authStepService.findCreationFunnelForEmail(player.email)?.let {
                it.race = race
            }
            createOkPreAuthResponse(request, "ok.")
        } ?: createErrorPreAuthResponse(request, "that is not a valid race.")
    }

    override fun getNextAuthStep(): AuthStep {
        return SpecializationAuthStep(authStepService, player)
    }
}
