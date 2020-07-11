package kotlinmud.action.impl

import kotlinmud.action.helper.mustBeAwake
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.availableInventoryAndItem
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.item.dao.ItemDAO

fun createPutAction(): Action {
    return Action(Command.PUT, mustBeAwake(), availableInventoryAndItem()) {
        val item = it.get<ItemDAO>(Syntax.ITEM_IN_INVENTORY)
        val container = it.get<ItemDAO>(Syntax.AVAILABLE_ITEM_INVENTORY)
        it.putItemInContainer(item, container)
        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("you put $item into $container.")
                .toObservers("${it.getMob()} puts $item into $container.")
                .build()
        )
    }
}
