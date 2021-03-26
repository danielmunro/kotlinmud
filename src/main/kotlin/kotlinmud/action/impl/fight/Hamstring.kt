package kotlinmud.action.impl.fight

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.affect.factory.createAffect
import kotlinmud.affect.type.AffectType
import kotlinmud.io.factory.target
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.mob.model.Mob
import kotlinmud.mob.skill.factory.mvCostOf
import kotlinmud.mob.skill.impl.Hamstring
import kotlinmud.mob.type.Intent

fun createHamstringAction(): Action {
    return ActionBuilder(Command.HAMSTRING).also {
        it.syntax = target()
        it.costs = listOf(mvCostOf(100))
        it.intent = Intent.OFFENSIVE
        it.skill = Hamstring()
    } build {
        createAffect(AffectType.STUNNED, Math.max(it.getLevel() / 10, 3))
        val mob = it.getMob()
        val target = it.get<Mob>(Syntax.TARGET_MOB)
        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("You slash $target's hamstring, disabling them.")
                .toTarget("$mob slashes your hamstring, disabling your movement.")
                .toObservers("$mob slashes $target's hamstring, disabling them.")
                .build()
        )
    }
}
