package kotlinmud.player.auth.impl

import kotlinmud.io.model.Client
import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.type.IOStatus
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.auth.service.CustomizationService
import kotlinmud.player.auth.type.AuthStep
import kotlinmud.player.auth.type.AuthorizationStep
import kotlinmud.player.dao.PlayerDAO
import org.jetbrains.exposed.sql.transactions.transaction

class CustomizationAuthStep(private val authStepService: AuthStepService, private val player: PlayerDAO) : AuthStep {
    override val authorizationStep = AuthorizationStep.CUSTOMIZE
    override val promptMessage = "What would you like to do (add/remove/list/help/done)?"
    override val errorMessage = "What was that?"
    private var done = false
    private val customizationService: CustomizationService

    init {
        val funnel = authStepService.findCreationFunnelForEmail(player.email)!!
        customizationService = CustomizationService(funnel.mobName)
    }

    override fun handlePreAuthRequest(request: PreAuthRequest): IOStatus {
        when (request.input.split(" ").first()) {
            "add" -> TODO()
            "remove" -> TODO()
            "list" -> list(request.client)
            "help" -> help(request.client)
            "done" -> done = true
            else -> return IOStatus.ERROR
        }

        return IOStatus.OK
    }

    override fun getNextAuthStep(): AuthStep {
        return if (done) {
            val funnel = authStepService.findCreationFunnelForEmail(player.email)!!
            val mob = funnel.build()
            CompleteAuthStep(transaction { mob.mobCard!! })
        } else {
            this
        }
    }

    private fun help(client: Client) {
        client.write("help text!1")
    }

    private fun list(client: Client) {
        client.write(
"""
Skills
======


Spells
======


Current experience to level: ${customizationService.getPoints() * 1000}
"""
        )
    }

    private fun add() {
    }

    private fun remove() {
    }
}
