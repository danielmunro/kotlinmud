package kotlinmud.action.impl

import kotlinmud.action.*
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.io.createResponseWithEmptyActionContext
import kotlinmud.item.Item

fun createInventoryAction(): Action {
    return Action(
        Command.INVENTORY,
        mustBeAwake(),
        listOf(Syntax.COMMAND),
        { _: ActionContextService, request: Request ->
            val items = request.mob.inventory.items.toList()
            createResponseWithEmptyActionContext(
                Message("Your inventory:\n\n${describeItems(items)}"))
        })
}

fun describeItems(items: List<Item>): String {
    return items.joinToString("\n") { it.name }
}
