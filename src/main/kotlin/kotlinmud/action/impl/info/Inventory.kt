package kotlinmud.action.impl.info

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeAwake
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
    return items.groupBy { it.id }
        .map { if (it.value.size > 1) "(${it.value.size}) " else "" + it.value[0].name }
        .joinToString("\n") { it }
}
