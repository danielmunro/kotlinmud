package kotlinmud.action.impl

import kotlinmud.action.helper.mustBeStanding
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.type.Attribute
import kotlinmud.io.factory.trainable
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import org.jetbrains.exposed.sql.transactions.transaction

fun createTrainAction(): Action {
    return Action(Command.TRAIN, mustBeStanding(), trainable()) {
        val attribute: Attribute = it.get(Syntax.TRAINABLE)
        val card = it.getMobCard()
        card.trains -= 1
        transaction {
            val attributes = AttributesDAO.new {
                mobCard = card.id
            }
            attributes.setAttribute(attribute, if (attribute.isVitals()) 10 else 1)
        }
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
