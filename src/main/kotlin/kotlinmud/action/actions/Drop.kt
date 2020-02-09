package kotlinmud.action.actions

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.ContextCollection
import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.item.Item
import kotlinmud.mob.Disposition
import org.jetbrains.exposed.sql.transactions.transaction

fun createDropAction(): Action {
    return Action(
        Command.DROP,
        arrayOf(Disposition.SITTING, Disposition.STANDING, Disposition.FIGHTING),
        arrayOf(Syntax.COMMAND, Syntax.ITEM_IN_INVENTORY),
        { _: ActionContextService, context: ContextCollection, request: Request ->
            val item = context.getResultBySyntax<Item>(Syntax.ITEM_IN_ROOM)!!
            transaction {
                item.inventory = request.room.inventory
            }
            Response(request, "foo")
        })
}