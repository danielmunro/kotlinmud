package kotlinmud.mob.skill.impl.healing

import kotlinmud.action.service.ActionContextService
import kotlinmud.affect.type.Affect
import kotlinmud.helper.math.dice
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.model.Response
import kotlinmud.io.type.Syntax
import kotlinmud.mob.skill.factory.clericAt
import kotlinmud.mob.skill.factory.easyForCleric
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.CostType
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.skill.type.SpellAction
import kotlinmud.mob.type.Intent

class Heal : SpellAction {
    override val affect: Affect? = null
    override val type = SkillType.HEAL
    override val levelObtained = mapOf(clericAt(30))
    override val difficulty = mapOf(easyForCleric())
    override val costs = listOf(
        Cost(CostType.DELAY, 1),
        Cost(CostType.MANA_AMOUNT, 50)
    )
    override val intent = Intent.PROTECTIVE

    override fun invoke(actionContextService: ActionContextService): Response {
        val amount = dice(2, 20) + 50
        val target = actionContextService.get(Syntax.OPTIONAL_TARGET) ?: actionContextService.getMob()
        target.increaseHp(amount)
        return actionContextService.createOkResponse(
            MessageBuilder()
                .toActionCreator("$target glows with rejuvenation.")
                .toTarget("you glow with rejuvenation.")
                .toObservers("$target glows with rejuvenation.")
                .build()
        )
    }
}