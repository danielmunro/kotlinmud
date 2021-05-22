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
import kotlinmud.mob.skill.impl.Bite
import kotlinmud.mob.type.Intent
import kotlin.random.Random

fun createBiteAction(): Action {
    return ActionBuilder(Command.BITE).also {
        it.syntax = optionalTarget()
        it.costs = listOf(mvCostOf(20))
        it.intent = Intent.OFFENSIVE
        it.skill = Bite()
    } build {
        val target = it.get<Mob>(Syntax.OPTIONAL_TARGET)
        val limit = (it.getLevel() / 10).coerceAtLeast(2)
        target.hp -= Random.nextInt(1, limit) +
            if (target.savesAgainst(DamageType.PIERCE)) 0 else Random.nextInt(1, limit)
        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("You bite $target.")
                .toTarget("${it.getMob()} bites you.")
                .toObservers("${it.getMob()} bites $target.")
                .build(),
            1
        )
    }
}
