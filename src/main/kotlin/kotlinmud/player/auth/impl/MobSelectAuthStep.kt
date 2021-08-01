package kotlinmud.player.auth.impl

import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.type.IOStatus
import kotlinmud.mob.model.PlayerMob
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.auth.type.AuthStep
import kotlinmud.player.auth.type.AuthorizationStep
import kotlinmud.player.dao.PlayerDAO

class MobSelectAuthStep(
    private val authStepService: AuthStepService,
    private val player: PlayerDAO
) : AuthStep {
    override val authorizationStep = AuthorizationStep.MOB_SELECT
    override val promptMessage = "by what name do you wish to be known?"
    override val errorMessage = "either that name is invalid or unavailable"
    private var newMob = false
    private var name = ""
    private var playerMob: PlayerMob? = null

    override fun handlePreAuthRequest(request: PreAuthRequest): IOStatus {
        return authStepService.findPlayerMobByName(request.input)?.let {
            playerMob = it
            validateMobBelongsToPlayer(it)
        } ?: createNewMob(request.input)
    }

    override fun getNextAuthStep(): AuthStep {
        return if (newMob)
            NewPlayerMobConfirmAuthStep(authStepService, player, name)
        else
            CompleteAuthStep(playerMob!!)
    }

    private fun validateMobBelongsToPlayer(mob: PlayerMob): IOStatus {
        return if (mob.accountName == player.name) IOStatus.OK else IOStatus.ERROR
    }

    private fun createNewMob(name: String): IOStatus {
        newMob = true
        this.name = name
        return IOStatus.OK
    }
}
