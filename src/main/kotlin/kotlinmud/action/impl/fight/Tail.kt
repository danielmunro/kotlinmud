package kotlinmud.action.impl.fight

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.affect.model.Affect
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.type.Attribute
import kotlinmud.helper.math.dice
import kotlinmud.io.factory.target
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.mob.model.Mob
import kotlinmud.mob.skill.factory.mvCostOf
import kotlinmud.mob.type.Intent

fun createTailAction(): Action {
    return ActionBuilder(Command.TAIL).also {
        it.syntax = target()
        it.costs = listOf(mvCostOf(20))
        it.intent = Intent.OFFENSIVE
    } build {
        val target = it.get<Mob>(Syntax.TARGET_MOB)

        if (dice(1, 4) == 1) {
            target.affects.add(Affect(AffectType.STUNNED, 0, mapOf(Pair(Attribute.STR, -1))))
        }

        val damage = dice(2, 4)
        target.hp -= damage
        val mob = it.getMob()

        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("you slap $target with your tail!")
                .toTarget("$mob slaps you with its tail!")
                .toObservers("$mob slaps $target with its tail!")
                .build()
        )
    }
}
