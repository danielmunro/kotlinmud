package kotlinmud.action.impl.fight

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.affect.model.Affect
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.type.Attribute
import kotlinmud.io.factory.optionalTarget
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.model.Mob
import kotlinmud.mob.skill.factory.mvCostOf
import kotlinmud.mob.skill.impl.Bash
import kotlinmud.mob.type.Intent
import kotlin.random.Random

fun createBashAction(): Action {
    return ActionBuilder(Command.BASH).also {
        it.costs = listOf(mvCostOf(20))
        it.syntax = optionalTarget()
        it.intent = Intent.OFFENSIVE
        it.skill = Bash()
    } build {
        val target = it.get<Mob>(Syntax.OPTIONAL_TARGET)
        val limit = (it.getLevel() / 10).coerceAtLeast(2)
        val modifier = Random.nextInt(1, limit) +
            if (target.savesAgainst(DamageType.POUND)) 0 else Random.nextInt(1, limit)
        target.hp -= modifier
        target.affects.add(
            Affect(
                AffectType.STUNNED,
                modifier / 5,
                mapOf(Pair(Attribute.INT, -1))
            )
        )
        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("you slam into $target and send them flying!")
                .toTarget("${it.getMob()} slams into you and sends you flying!")
                .toObservers("${it.getMob()} slams into $target and sends them flying!")
                .build(),
            1
        )
    }
}
