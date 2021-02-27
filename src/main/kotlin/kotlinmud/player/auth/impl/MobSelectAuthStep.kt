package kotlinmud.player.auth.impl

import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.type.IOStatus
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.auth.type.AuthStep
import kotlinmud.player.auth.type.AuthorizationStep
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.player.dao.PlayerDAO
import org.jetbrains.exposed.sql.transactions.transaction

class MobSelectAuthStep(
    private val authStepService: AuthStepService,
    private val player: PlayerDAO
) : AuthStep {
    override val authorizationStep = AuthorizationStep.MOB_SELECT
    override val promptMessage = "by what name do you wish to be known?"
    override val errorMessage = "either that name is invalid or unavailable"
    private var newMob = false
    private var name = ""
    private var mobCard: MobCardDAO? = null

    override fun handlePreAuthRequest(request: PreAuthRequest): IOStatus {
        return authStepService.findMobCardByName(request.input)?.let {
            mobCard = it
            validateMobBelongsToPlayer(it)
        } ?: createNewMob(request.input)
    }

    override fun getNextAuthStep(): AuthStep {
        return if (newMob)
            NewMobCardConfirmAuthStep(authStepService, player, name)
        else
            CompleteAuthStep(mobCard!!)
    }

    private fun validateMobBelongsToPlayer(mobCard: MobCardDAO): IOStatus {
        return if (transaction { mobCard.player } == player) IOStatus.OK else IOStatus.ERROR
    }

    private fun createNewMob(name: String): IOStatus {
        newMob = true
        this.name = name
        return IOStatus.OK
    }
}
