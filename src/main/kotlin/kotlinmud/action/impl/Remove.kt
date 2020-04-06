package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.item.Item

fun createRemoveAction(): Action {
    return Action(
        Command.REMOVE,
        mustBeAlert(),
        listOf(Syntax.COMMAND, Syntax.EQUIPPED_ITEM),
        { svc: ActionContextService, request: Request ->
            val item = svc.get<Item>(Syntax.EQUIPPED_ITEM)
            request.mob.equipped.remove(item)
            svc.createResponse(Message(
                "you remove $item and put it in your inventory.",
                "${request.mob} removes $item and puts it in their inventory."
            ))
        })
}
