package kotlinmud.action.impl.fight

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.affect.model.Affect
import kotlinmud.affect.type.AffectType
import kotlinmud.helper.math.dice
import kotlinmud.io.factory.optionalTarget
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.mob.model.Mob
import kotlinmud.mob.skill.factory.mvCostOf
import kotlinmud.mob.skill.impl.Trip
import kotlinmud.mob.type.Intent

fun createTripAction(): Action {
    return ActionBuilder(Command.TRIP).also {
        it.syntax = optionalTarget()
        it.costs = listOf(mvCostOf(20))
        it.intent = Intent.OFFENSIVE
        it.skill = Trip()
    } build {
        val mob = it.getMob()
        val target = it.get<Mob>(Syntax.OPTIONAL_TARGET)
        target.affects.add(Affect(AffectType.STUNNED, 0))
        target.hp -= dice(2, 4)
        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("you trip $target and they go down hard.")
                .toTarget("$mob trips you!")
                .toObservers("$mob trips $target.")
                .build()
        )
    }
}
