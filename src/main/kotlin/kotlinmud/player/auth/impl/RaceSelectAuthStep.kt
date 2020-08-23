package kotlinmud.player.auth.impl

import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.type.IOStatus
import kotlinmud.mob.race.factory.matchPlayableRace
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.auth.type.AuthStep
import kotlinmud.player.auth.type.AuthorizationStep
import kotlinmud.player.dao.PlayerDAO

class RaceSelectAuthStep(private val authStepService: AuthStepService, private val player: PlayerDAO) : AuthStep {
    override val authorizationStep = AuthorizationStep.RACE_SELECT
    override val promptMessage = "select a race:"
    override val errorMessage = "that is not a race. Enter 'help race' for help."

    override fun handlePreAuthRequest(request: PreAuthRequest): IOStatus {
        return matchPlayableRace(request.input)?.let { race ->
            authStepService.findCreationFunnelForEmail(player.email)?.let {
                it.race = race
            }
            IOStatus.OK
        } ?: IOStatus.ERROR
    }

    override fun getNextAuthStep(): AuthStep {
        return SpecializationAuthStep(authStepService, player)
    }
}
