package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeStanding
import kotlinmud.attributes.Attribute
import kotlinmud.attributes.AttributesBuilder
import kotlinmud.attributes.isVitals
import kotlinmud.attributes.setAttribute
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax

fun createTrainAction(): Action {
    return Action(
        Command.TRAIN,
        mustBeStanding(),
        listOf(Syntax.COMMAND, Syntax.TRAINABLE),
        { svc: ActionContextService, request: Request ->
            val attribute: Attribute = svc.get(Syntax.TRAINABLE)
            request.mob.trains -= 1
            request.mob.trainedAttributes.add(
                setAttribute(AttributesBuilder(), attribute, if (isVitals(attribute)) 10 else 1).build()
            )
            svc.createResponse(
                Message(
                    "you train your ${getImprove(attribute)}.",
                    "${request.mob.name} trains their ${getImprove(attribute)}."
                )
            )
        })
}

fun getImprove(attribute: Attribute): String {
    return when (attribute) {
        Attribute.HP -> "health"
        Attribute.MANA -> "mana"
        Attribute.MV -> "movement"
        Attribute.STR -> "strength"
        Attribute.INT -> "intelligence"
        Attribute.WIS -> "wisdom"
        Attribute.DEX -> "dexterity"
        Attribute.CON -> "constitution"
        else -> "attributes"
    }
}
