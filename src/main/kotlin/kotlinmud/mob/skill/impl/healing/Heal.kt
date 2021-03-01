package kotlinmud.mob.skill.impl.healing

import kotlinmud.action.service.ActionContextService
import kotlinmud.affect.type.AffectInterface
import kotlinmud.helper.math.dice
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.model.Response
import kotlinmud.io.type.Syntax
import kotlinmud.mob.skill.factory.clericAt
import kotlinmud.mob.skill.factory.delayCostOf
import kotlinmud.mob.skill.factory.easyForCleric
import kotlinmud.mob.skill.factory.manaCostOf
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.skill.type.SpellAction
import kotlinmud.mob.type.Intent

class Heal : SpellAction {
    override val affect: AffectInterface? = null
    override val type = SkillType.HEAL
    override val levelObtained = mapOf(clericAt(30))
    override val difficulty = mapOf(easyForCleric())
    override val costs = listOf(
        delayCostOf(1),
        manaCostOf(100)
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
