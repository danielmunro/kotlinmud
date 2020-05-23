package kotlinmud.player.authStep.impl

import kotlinmud.io.IOStatus
import kotlinmud.io.PreAuthRequest
import kotlinmud.io.PreAuthResponse
import kotlinmud.player.authStep.AuthStep
import kotlinmud.player.authStep.AuthStepService
import kotlinmud.player.authStep.AuthorizationStep
import kotlinmud.player.model.MobCard
import kotlinmud.player.model.Player

class MobSelectAuthStep(
    private val authStepService: AuthStepService,
    private val player: Player
) : AuthStep {
    override val authorizationStep: AuthorizationStep = AuthorizationStep.MOB_SELECT
    override val promptMessage: String = "by what name do you wish to be known?"
    override val errorMessage: String = "that name is not valid"
    private var newMob: Boolean = false
    private var mobCard: MobCard? = null

    override fun handlePreAuthRequest(request: PreAuthRequest): PreAuthResponse {
        return authStepService.findMobCardByName(request.input)?.let {
            if (player.email == it.playerEmail) {
                mobCard = it
                PreAuthResponse(request, IOStatus.OK, "ok")
            } else {
                PreAuthResponse(request, IOStatus.ERROR, "that name is not available")
            }
        } ?: run {
            newMob = true
            PreAuthResponse(request, IOStatus.OK, "new mob.")
        }
    }

    override fun getNextAuthStep(): AuthStep {
        return if (newMob) NewMobCardAuthStep() else CompleteAuthStep(mobCard!!)
    }
}
