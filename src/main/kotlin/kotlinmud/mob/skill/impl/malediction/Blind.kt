package kotlinmud.mob.skill.impl.healing

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.service.ActionContextService
import kotlinmud.action.type.Command
import kotlinmud.affect.factory.createAffect
import kotlinmud.affect.impl.BlindAffect
import kotlinmud.affect.type.AffectType
import kotlinmud.io.factory.offensiveSpell
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.model.Response
import kotlinmud.io.type.Syntax
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.skill.factory.easyForCleric
import kotlinmud.mob.skill.factory.mageAt
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.CostType
import kotlinmud.mob.skill.type.SkillAction
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Intent
import org.jetbrains.exposed.sql.transactions.transaction

class Blind : SkillAction {
    override val affect = BlindAffect()
    override val type = SkillType.BLIND
    override val levelObtained = mapOf(mageAt(5))
    override val difficulty = mapOf(easyForCleric())
    override val dispositions = mustBeAlert()
    override val costs = listOf(
        Cost(CostType.DELAY, 1),
        Cost(CostType.MANA_AMOUNT, 50)
    )
    override val intent = Intent.OFFENSIVE
    override val invokesOn = SkillInvokesOn.INPUT
    override val syntax = offensiveSpell()
    override val argumentOrder = listOf(1, 2, 3)
    override val command = Command.CAST

    override fun invoke(actionContextService: ActionContextService): Response {
        val target = actionContextService.get<MobDAO>(Syntax.TARGET_MOB)
        createAffect(AffectType.BLIND, actionContextService.getLevel()).also {
            transaction {
                it.mob = actionContextService.getMob()
            }
        }
        return actionContextService.createOkResponse(
            MessageBuilder()
                .toActionCreator("$target is blind!")
                .toTarget("you are blind!")
                .toObservers("$target is blinded!")
                .build()
        )
    }
}
