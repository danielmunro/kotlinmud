package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.item.Item

fun createWearAction(): Action {
    return Action(
        Command.WEAR,
        mustBeAlert(),
        listOf(Syntax.COMMAND, Syntax.EQUIPMENT_IN_INVENTORY),
        { svc: ActionContextService, request: Request ->
            val item = svc.get<Item>(Syntax.EQUIPMENT_IN_INVENTORY)
            val removed = request.mob.equipped.items.find {
                it.position == item.position
            }?.let {
                request.mob.equipped.items.remove(it)
            }
            request.mob.inventory.items.remove(item)
            request.mob.equipped.items.add(item)
            svc.createResponse(
                Message(
                    "you ${if (removed != null) "remove $removed and " else ""}wear $item.",
                    "${request.mob} ${if (removed != null) "removes $removed and " else "" }wears $item."
                )
            )
        })
}
