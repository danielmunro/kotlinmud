package kotlinmud.action.impl.player

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createDrinkMessage
import kotlinmud.io.factory.drink
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.type.Syntax
import kotlinmud.item.helper.applyAffectFromItem
import kotlinmud.item.model.Item

fun createDrinkAction(): Action {
    return ActionBuilder(Command.DRINK).also {
        it.syntax = drink()
    } build {
        val item = it.get<Item>(Syntax.AVAILABLE_DRINK)
        val mob = it.getMob()
        if (mob.isFull()) {
            return@build it.createErrorResponse(messageToActionCreator("you are full."))
        }

        if (item.quantity != null) {
            item.quantity = item.quantity!! - 1
        }
        mob.thirst += 50
        applyAffectFromItem(mob, item)

        it.createOkResponse(createDrinkMessage(mob, item))
    }
}
