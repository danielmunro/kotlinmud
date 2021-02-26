package kotlinmud.action.impl.item

import kotlinmud.action.helper.mustBeAwake
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createDropMessage
import kotlinmud.io.factory.itemInInventory
import kotlinmud.io.type.Syntax
import kotlinmud.item.dao.ItemDAO

fun createDropAction(): Action {
    return Action(Command.DROP, mustBeAwake(), itemInInventory()) {
        val item = it.get<ItemDAO>(Syntax.ITEM_IN_INVENTORY)
        val mob = it.getMob()
        mob.items.remove(item)
        it.putItemInRoom(item, it.getRoom())
        it.createOkResponse(createDropMessage(mob, item))
    }
}
