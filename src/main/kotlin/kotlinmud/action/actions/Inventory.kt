package kotlinmud.action.actions

import kotlinmud.action.*
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.io.createResponseWithEmptyActionContext
import kotlinmud.item.ItemEntity
import org.jetbrains.exposed.sql.transactions.transaction

fun createInventoryAction(): Action {
    return Action(
        Command.INVENTORY,
        mustBeAlive(),
        listOf(Syntax.COMMAND),
        { _: ActionContextService, _: ActionContextList, request: Request ->
            val items = transaction { request.mob.inventory.items.toList() }
            createResponseWithEmptyActionContext(Message("Your inventory:\n\n${describeItems(items)}"))
        })
}

fun describeItems(items: List<ItemEntity>): String {
    return items.joinToString("\n") { it.name }
}
