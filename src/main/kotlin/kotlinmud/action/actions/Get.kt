package kotlinmud.action.actions

import kotlinmud.EventService
import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.ContextCollection
import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.item.Item
import kotlinmud.mob.Disposition

fun createGetAction(): Action {
    return Action(
        Command.GET,
        arrayOf(Disposition.SITTING, Disposition.STANDING, Disposition.FIGHTING),
        arrayOf(Syntax.COMMAND, Syntax.ITEM_IN_ROOM),
        { _: EventService, context: ContextCollection, request: Request ->
            val item = context.getResultBySyntax<Item>(Syntax.ITEM_IN_ROOM)
//            item?.inventory = request.mob.inventory
            Response(request, "foo")
        })
}