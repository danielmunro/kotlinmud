package kotlinmud.action.actions

import kotlinmud.action.*
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.item.Item

fun createGetAction(): Action {
    return Action(
        Command.GET,
        mustBeAwake(),
        listOf(Syntax.COMMAND, Syntax.ITEM_IN_ROOM),
        { svc: ActionContextService, request: Request ->
            val item = svc.get<Item>(Syntax.ITEM_IN_ROOM)
            request.room.inventory.items.remove(item)
            request.mob.inventory.items.add(item)
            svc.createResponse(Message(
                    "you pick up ${item.name}.",
                    "${request.mob.name} picks up ${item.name}."))
        })
}
