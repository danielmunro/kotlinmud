package kotlinmud.mob.skill.impl.healing

import kotlinmud.action.service.ActionContextService
import kotlinmud.affect.type.AffectInterface
import kotlinmud.helper.math.dice
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.model.Response
import kotlinmud.io.type.Syntax
import kotlinmud.mob.skill.factory.clericAt
import kotlinmud.mob.skill.factory.easyForCleric
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.skill.type.SpellAction
import kotlinmud.mob.type.Intent

class CureSerious : SpellAction {
    override val affect: AffectInterface? = null
    override val type = SkillType.CURE_SERIOUS
    override val levelObtained = mapOf(clericAt(10))
    override val difficulty = mapOf(easyForCleric())
    override val intent = Intent.PROTECTIVE

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
