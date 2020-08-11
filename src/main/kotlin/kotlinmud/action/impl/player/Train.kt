package kotlinmud.action.impl.player

import kotlinmud.action.helper.mustBeStanding
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.attributes.type.Attribute
import kotlinmud.io.factory.createTrainMessage
import kotlinmud.io.factory.trainable
import kotlinmud.io.type.Syntax

fun createTrainAction(): Action {
    return Action(Command.TRAIN, mustBeStanding(), trainable()) {
        with(it.get<Attribute>(Syntax.TRAINABLE)) {
            it.train(this)
            it.createOkResponse(createTrainMessage(it.getMob(), this))
        }
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
