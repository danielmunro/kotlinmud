package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAwake
import kotlinmud.io.Message
import kotlinmud.io.Syntax
import kotlinmud.item.Item

fun createGetAction(): Action {
    return Action(
        Command.GET,
        mustBeAwake(),
        listOf(Syntax.COMMAND, Syntax.ITEM_IN_ROOM)) {
            val item = it.get<Item>(Syntax.ITEM_IN_ROOM)
            it.changeItemOwner(item, it.getMob())
            it.createResponse(Message(
                "you pick up ${item.name}.",
                "${it.getMob()} picks up ${item.name}."))
        }
}
