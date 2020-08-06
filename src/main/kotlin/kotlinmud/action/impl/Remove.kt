package kotlinmud.action.impl

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createRemoveMessage
import kotlinmud.io.factory.equippedItem
import kotlinmud.io.type.Syntax
import kotlinmud.item.dao.ItemDAO
import org.jetbrains.exposed.sql.transactions.transaction

fun createRemoveAction(): Action {
    return Action(Command.REMOVE, mustBeAlert(), equippedItem()) {
        val item = it.get<ItemDAO>(Syntax.EQUIPPED_ITEM)
        transaction { item.mobEquipped = null }
        it.createOkResponse(createRemoveMessage(it.getMob(), item))
    }
}
