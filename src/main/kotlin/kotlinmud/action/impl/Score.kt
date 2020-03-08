package kotlinmud.action.impl

import kotlinmud.action.*
import kotlinmud.attributes.Attribute
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.io.createResponseWithEmptyActionContext

fun createScoreAction(): Action {
    return Action(
        Command.SCORE,
        mustBeAlive(),
        listOf(Syntax.COMMAND),
        { _: ActionContextService, request: Request ->
            val mob = request.mob
            createResponseWithEmptyActionContext(
                Message(
                    "You are $mob, unknown years old.\n" +
                            "hp, mana, mv status lines.\n" +
                            "You have ${mob.hp} of ${mob.calc(Attribute.HP)} hit points," +
                            "${mob.mana} of ${mob.calc(Attribute.MANA)} mana, " +
                            "${mob.mv} of ${mob.calc(Attribute.MV)} moves.\n" +
                            "Race: ${mob.race.type.toString().toLowerCase()} Sex: $mob"
                )
            )
        }
    )
}
