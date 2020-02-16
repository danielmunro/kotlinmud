package kotlinmud.action.actions

import kotlinmud.action.Action
import kotlinmud.action.ActionContextList
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.io.*
import kotlinmud.item.ItemEntity
import kotlinmud.mob.Disposition
import org.jetbrains.exposed.sql.transactions.transaction

fun createInventoryAction(): Action {
    return Action(
        Command.INVENTORY,
        arrayOf(Disposition.SITTING, Disposition.STANDING, Disposition.FIGHTING),
        arrayOf(Syntax.COMMAND),
        { _: ActionContextService, _: ActionContextList, request: Request ->
            val items = transaction { request.mob.inventory.items.toList() }
            createResponseWithEmptyActionContext(Message("Your inventory:\n\n${describeItems(items)}"))
        })
}

fun describeItems(items: List<ItemEntity>): String {
    return items.joinToString("\n") { it.name }
}
