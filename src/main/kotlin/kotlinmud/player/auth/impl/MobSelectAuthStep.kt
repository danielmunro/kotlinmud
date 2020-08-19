package kotlinmud.player.auth.impl

import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.type.IOStatus
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.auth.type.AuthStep
import kotlinmud.player.auth.type.AuthorizationStep
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.player.dao.PlayerDAO

class MobSelectAuthStep(
    private val authStepService: AuthStepService,
    private val player: PlayerDAO
) : AuthStep {
    override val authorizationStep = AuthorizationStep.MOB_SELECT
    override val promptMessage = "by what name do you wish to be known?"
    override val errorMessage = "that name is not valid"
    private var newMob = false
    private var name = ""
    private var mobCard: MobCardDAO? = null

    override fun handlePreAuthRequest(request: PreAuthRequest): IOStatus {
        return authStepService.findMobCardByName(request.input)?.let {
            val mob = player.mobs.find { mob -> mob.mobCard?.id?.value == mobCard?.id?.value }
            if (mob != null) {
                mobCard = it
                IOStatus.OK
            } else {
                IOStatus.ERROR
            }
        } ?: run {
            newMob = true
            name = request.input
            IOStatus.OK
        }
    }

    override fun getNextAuthStep(): AuthStep {
        return if (newMob) NewMobCardAuthStep(authStepService, player, name) else CompleteAuthStep(mobCard!!)
    }
}
