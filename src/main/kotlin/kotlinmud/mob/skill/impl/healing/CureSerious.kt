package kotlinmud.mob.skill.impl.healing

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.service.ActionContextService
import kotlinmud.action.type.Command
import kotlinmud.affect.type.Affect
import kotlinmud.helper.math.dice
import kotlinmud.io.factory.spell
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.model.Response
import kotlinmud.io.type.Syntax
import kotlinmud.mob.skill.factory.clericAt
import kotlinmud.mob.skill.factory.easyForCleric
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.CostType
import kotlinmud.mob.skill.type.SkillAction
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Intent

class CureSerious : SkillAction {
    override val affect: Affect? = null
    override val type = SkillType.CURE_SERIOUS
    override val levelObtained = mapOf(clericAt(10))
    override val difficulty = mapOf(easyForCleric())
    override val dispositions = mustBeAlert()
    override val costs = listOf(
        Cost(CostType.DELAY, 1),
        Cost(CostType.MANA_AMOUNT, 50)
    )
    override val intent = Intent.PROTECTIVE
    override val invokesOn = SkillInvokesOn.INPUT
    override val syntax = spell()
    override val argumentOrder = listOf(1, 2, 3)
    override val command = Command.CAST

    override fun invoke(actionContextService: ActionContextService): Response {
        val amount = dice(2, 6) + Math.max(actionContextService.getMob().level / 2, 15)
        val target = actionContextService.get(Syntax.OPTIONAL_TARGET) ?: actionContextService.getMob()
        target.increaseHp(amount)
        return actionContextService.createOkResponse(
            MessageBuilder()
                .toActionCreator("$target feels a warm rush!")
                .toTarget("you feel a warm rush!")
                .toObservers("$target feels a warm rush!")
                .build()
        )
    }
}
