package kotlinmud.action.impl

import kotlinmud.action.helper.mustBeAwake
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createDrinkMessage
import kotlinmud.io.factory.drink
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.type.Syntax
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.helper.applyAffectFromItem
import org.jetbrains.exposed.sql.transactions.transaction

fun createDrinkAction(): Action {
    return Action(Command.DRINK, mustBeAwake(), drink()) {
        val item = it.get<ItemDAO>(Syntax.AVAILABLE_DRINK)
        val mobCard = it.getMobCard()
        if (mobCard.isFull(it.getMob().race)) {
            return@Action it.createErrorResponse(messageToActionCreator("you are full."))
        }

        transaction {
            item.quantity?.let { quantity ->
                item.quantity = quantity - 1
            }
            mobCard.thirst += 1
        }
        applyAffectFromItem(it.getMob(), item)

        it.createOkResponse(createDrinkMessage(it.getMob(), item))
    }
}
