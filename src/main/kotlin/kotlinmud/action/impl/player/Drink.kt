package kotlinmud.action.impl.player

import kotlinmud.action.helper.mustBeAwake
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createDrinkMessage
import kotlinmud.io.factory.drink
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.type.Syntax
import kotlinmud.item.helper.applyAffectFromItem
import kotlinmud.item.model.Item

fun createDrinkAction(): Action {
    return Action(Command.DRINK, mustBeAwake(), drink()) {
        val item = it.get<Item>(Syntax.AVAILABLE_DRINK)
        val mob = it.getMob()
        if (mob.isFull()) {
            return@Action it.createErrorResponse(messageToActionCreator("you are full."))
        }

        item.quantity = item.quantity!! - 1
        mob.thirst += 1
        applyAffectFromItem(mob, item)

        it.createOkResponse(createDrinkMessage(mob, item))
    }
}
