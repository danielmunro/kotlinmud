package kotlinmud.action.impl.fight

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.optionalTarget
import kotlinmud.io.factory.target
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.item.type.Position
import kotlinmud.mob.model.Mob
import kotlinmud.mob.skill.impl.Disarm
import kotlinmud.mob.type.Intent

fun createDisarmAction(): Action {
    return ActionBuilder(Command.DISARM).also {
        it.syntax = optionalTarget()
        it.intent = Intent.OFFENSIVE
        it.skill = Disarm()
    } build {
        val mob = it.getMob()
        val target = it.get<Mob>(Syntax.OPTIONAL_TARGET)
        target.getEquippedByPosition(Position.WEAPON)?.let { item ->
            target.equipped.remove(item)
            it.getRoom().items.add(item)
            it.createOkResponse(
                MessageBuilder()
                    .toActionCreator("You disarm $target and send their weapon flying!")
                    .toTarget("$mob disarms you and sends your weapon flying!")
                    .toObservers("$mob disarms $target and sends their weapon flying!")
                    .build()
            )
        } ?: run {
            it.createErrorResponse(
                MessageBuilder()
                    .toActionCreator("They are not equipped with a weapon.")
                    .build()
            )
        }
    }
}
