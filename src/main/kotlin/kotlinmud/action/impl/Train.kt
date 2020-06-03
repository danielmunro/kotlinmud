package kotlinmud.action.impl

import kotlinmud.action.model.Action
import kotlinmud.action.mustBeStanding
import kotlinmud.action.type.Command
import kotlinmud.attributes.model.AttributesBuilder
import kotlinmud.attributes.model.isVitals
import kotlinmud.attributes.model.setAttribute
import kotlinmud.attributes.type.Attribute
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.factory.trainable
import kotlinmud.io.type.Syntax

fun createTrainAction(): Action {
    return Action(Command.TRAIN, mustBeStanding(), trainable()) {
        val attribute: Attribute = it.get(Syntax.TRAINABLE)
        val card = it.getMobCard()
        card.trains -= 1
        card.trainedAttributes.add(
            setAttribute(
                AttributesBuilder(),
                attribute,
                if (isVitals(attribute)) 10 else 1
            ).build()
        )
        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("you train your ${getImprove(attribute)}.")
                .toObservers("${it.getMob()} trains their ${getImprove(attribute)}.")
                .build()
        )
    }
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
