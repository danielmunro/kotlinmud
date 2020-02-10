package kotlinmud.action.actions

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.ContextCollection
import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.item.ItemEntity
import kotlinmud.mob.Disposition
import org.jetbrains.exposed.sql.transactions.transaction

fun createInventoryAction(): Action {
    return Action(
        Command.INVENTORY,
        arrayOf(Disposition.SITTING, Disposition.STANDING, Disposition.FIGHTING),
        arrayOf(Syntax.COMMAND),
        { _: ActionContextService, _: ContextCollection, request: Request ->
            val items = transaction { request.mob.inventory.items.toList() }
            Response(request, "Your inventory:\n\n${describeItems(items)}")
        })
}

fun describeItems(items: List<ItemEntity>): String {
    return items.joinToString("\n") { it.name }
}
