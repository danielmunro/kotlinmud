package kotlinmud.action.impl.cast.healing

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.helper.math.dice
import kotlinmud.io.factory.spell
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.mob.skill.factory.manaCostOf

fun createCureLightAction(): Action {
    return ActionBuilder(Command.CURE_LIGHT).also {
        it.syntax = spell()
        it.costs = listOf(manaCostOf(100))
    } build {
        val amount = dice(1, 6)
        val target = it.get(Syntax.OPTIONAL_TARGET) ?: it.getMob()
        target.increaseHp(amount)
        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("$target feels better!")
                .toTarget("you feel better!")
                .toObservers("$target feels better!")
                .build(),
            1
        )
    }
}
