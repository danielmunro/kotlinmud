package kotlinmud.action.impl.item

import kotlinmud.action.helper.mustBeAwake
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createDropMessage
import kotlinmud.io.factory.itemInInventory
import kotlinmud.io.type.Syntax
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.model.Item

fun createDropAction(): Action {
    return Action(Command.DROP, mustBeAwake(), itemInInventory()) {
        val item = it.get<Item>(Syntax.ITEM_IN_INVENTORY)
        val mob = it.getMob()
        mob.items.remove(item)
        it.getRoom().items.plus(item)
        it.createOkResponse(createDropMessage(mob, item))
    }
}
