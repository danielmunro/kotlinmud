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
        val attribute = it.get<Attribute>(Syntax.TRAINABLE)
        val mob = it.getMob()
        mob.trains -= 1
        val amount = if (attribute.isVitals()) 10 else 1
        mob.attributes[attribute]?.let { initial ->
            mob.attributes[attribute] = initial + amount
        } ?: run { mob.attributes[attribute] = amount }
        it.createOkResponse(createTrainMessage(it.getMob(), attribute))
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
