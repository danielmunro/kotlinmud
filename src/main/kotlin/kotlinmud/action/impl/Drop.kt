package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeAwake
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.item.Item

fun createDropAction(): Action {
    return Action(
        Command.DROP,
        mustBeAwake(),
        listOf(Syntax.COMMAND, Syntax.ITEM_IN_INVENTORY),
        { svc: ActionContextService, request: Request ->
            val item = svc.get<Item>(Syntax.ITEM_IN_INVENTORY)
            request.mob.inventory.items.remove(item)
            request.room.inventory.items.add(item)
            svc.createResponse(
                Message("you drop ${item.name}.", "${request.mob.name} drops ${item.name}."))
        })
}
