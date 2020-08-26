package kotlinmud.mob.skill.impl.healing

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.service.ActionContextService
import kotlinmud.action.type.Command
import kotlinmud.affect.factory.createAffect
import kotlinmud.affect.impl.CurseAffect
import kotlinmud.affect.type.AffectType
import kotlinmud.io.factory.offensiveSpell
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.model.Response
import kotlinmud.io.type.Syntax
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.skill.factory.easyForMage
import kotlinmud.mob.skill.factory.mageAt
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.CostType
import kotlinmud.mob.skill.type.SkillAction
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Intent
import org.jetbrains.exposed.sql.transactions.transaction

class Curse : SkillAction {
    override val affect = CurseAffect()
    override val type = SkillType.CURSE
    override val levelObtained = mapOf(mageAt(15))
    override val difficulty = mapOf(easyForMage())
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
        createAffect(AffectType.CURSE, actionContextService.getLevel()).also {
            transaction {
                it.mob = actionContextService.getMob()
            }
        }
        return actionContextService.createOkResponse(
            MessageBuilder()
                .toActionCreator("$target is cursed!")
                .toTarget("you are cursed!")
                .toObservers("$target is cursed!")
                .build()
        )
    }
}
