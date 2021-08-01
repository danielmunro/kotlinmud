package kotlinmud.player.auth.impl

import kotlinmud.helper.string.leftPad
import kotlinmud.io.model.Client
import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.type.IOStatus
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.auth.service.CustomizationService
import kotlinmud.player.auth.type.AuthStep
import kotlinmud.player.auth.type.AuthorizationStep
import kotlinmud.player.dao.PlayerDAO

class CustomizationAuthStep(private val authStepService: AuthStepService, private val player: PlayerDAO) : AuthStep {
    override val authorizationStep = AuthorizationStep.CUSTOMIZE
    override val promptMessage = "What would you like to do (add/remove/list/help/done)?"
    override val errorMessage = "What was that?"
    private var done = false
    private val customizationService: CustomizationService

    init {
        val funnel = authStepService.findCreationFunnelForEmail(player.name)!!
        customizationService = CustomizationService(funnel.mobName)
    }

    override fun handlePreAuthRequest(request: PreAuthRequest): IOStatus {
        when (request.input.split(" ").first()) {
            "add" -> add(prepareFromPool(request) ?: return IOStatus.ERROR)
            "remove" -> remove(prepareFromAdded(request) ?: return IOStatus.ERROR)
            "list" -> list(request.client)
            "help" -> help(
                request.client,
                customizationService.findAddedCustomization(request.input)
                    ?: customizationService.findCustomizationFromPool(request.input) ?: return IOStatus.ERROR
            )
            "done" -> done = true
            else -> return IOStatus.ERROR
        }

        return IOStatus.OK
    }

    override fun getNextAuthStep(): AuthStep {
        return if (done) {
            val funnel = authStepService.findCreationFunnelForEmail(player.name)!!
            val mob = funnel.build(player)
            CompleteAuthStep(mob)
        } else {
            this
        }
    }

    private fun help(client: Client, customization: Customization) {
        client.write(customization.helpText)
    }

    private fun list(client: Client) {
        val unlearned = customizationService.getUnlearned()
        val spells = unlearned.filter { it.creationGroupType == CreationGroupType.SPELL_GROUP }
        val skills = unlearned.filter { it.creationGroupType == CreationGroupType.SKILL }
        val defaults = unlearned.filter { it.creationGroupType == CreationGroupType.DEFAULT_GROUP }
        client.write(
"""
Spell Groups
============
${format(spells)}

Skills
======
${format(skills)}

Defaults
========
${format(defaults)}

Current experience to level: ${customizationService.getPoints() * 1000}
"""
        )
    }

    private fun add(customization: Customization) {
        customizationService.add(customization)
    }

    private fun remove(customization: Customization) {
        customizationService.remove(customization)
    }

    private fun format(spellGroups: List<Customization>): String {
        var i = 0
        return spellGroups.fold("") { acc, it ->
            i++
            acc + leftPad(it.name + "  " + it.points, 25) + (if (i % 2 == 0) "\n" else "")
        }
    }

    private fun prepareFromPool(request: PreAuthRequest): Customization? {
        return customizationService.findCustomizationFromPool(
            request.arg(1) ?: run {
                request.client.write("that customization is not available from the pool.")
                return null
            }
        )
    }

    private fun prepareFromAdded(request: PreAuthRequest): Customization? {
        return customizationService.findAddedCustomization(
            request.arg(1) ?: run {
                request.client.write("that customization has not been added.")
                return null
            }
        )
    }
}
