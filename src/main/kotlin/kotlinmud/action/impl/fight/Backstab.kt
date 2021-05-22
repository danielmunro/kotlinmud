package kotlinmud.action.impl.fight

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.optionalTarget
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.model.Mob
import kotlinmud.mob.skill.factory.mvCostOf
import kotlinmud.mob.skill.impl.Backstab
import kotlinmud.mob.type.Intent
import kotlin.random.Random

fun createBackstabAction(): Action {
    return ActionBuilder(Command.BACK_STAB).also {
        it.syntax = optionalTarget()
        it.costs = listOf(mvCostOf(100))
        it.skill = Backstab()
        it.intent = Intent.OFFENSIVE
    } build {
        val target = it.get<Mob>(Syntax.OPTIONAL_TARGET)
        val limit = (it.getLevel() / 10).coerceAtLeast(10)
        target.hp -= Random.nextInt(1, limit) +
            if (target.savesAgainst(DamageType.PIERCE)) 0 else Random.nextInt(1, limit)
        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("You stab $target in the back.")
                .toTarget("${it.getMob()} stabs you in the back.")
                .toObservers("${it.getMob()} stabs $target in the back.")
                .build(),
            1
        )
    }
}
