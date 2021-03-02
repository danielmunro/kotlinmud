package kotlinmud.action.impl.player

import kotlinmud.action.helper.mustBeAwake
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createEatMessage
import kotlinmud.io.factory.foodInInventory
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.type.Syntax
import kotlinmud.item.helper.applyAffectFromItem
import kotlinmud.item.model.Item
import org.jetbrains.exposed.sql.transactions.transaction

fun createEatAction(): Action {
    return Action(Command.EAT, mustBeAwake(), foodInInventory()) {
        val item = it.get<Item>(Syntax.AVAILABLE_FOOD)
        val mobCard = it.getMobCard()
        val mob = it.getMob()

        if (mobCard.isFull(mob.race)) {
            return@Action it.createErrorResponse(messageToActionCreator("you are full."))
        }

        transaction { mobCard.hunger += 1 }
        applyAffectFromItem(mob, item)
        it.getMob().items.remove(item)

        it.createOkResponse(createEatMessage(mob, item))
    }
}
