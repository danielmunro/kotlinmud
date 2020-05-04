package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Message
import kotlinmud.io.Syntax
import kotlinmud.item.Item

fun createRemoveAction(): Action {
    return Action(
        Command.REMOVE,
        mustBeAlert(),
        listOf(Syntax.COMMAND, Syntax.EQUIPPED_ITEM)) {
            val item = it.get<Item>(Syntax.EQUIPPED_ITEM)
            it.getMob().equipped.remove(item)
            it.createResponse(Message(
                "you remove $item and put it in your inventory.",
                "${it.getMob()} removes $item and puts it in their inventory."
            ))
        }
}
