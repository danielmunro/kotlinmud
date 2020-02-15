package kotlinmud.action.actions

import kotlinmud.action.Action
import kotlinmud.action.ActionContextList
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.item.ItemEntity
import kotlinmud.mob.Disposition
import org.jetbrains.exposed.sql.transactions.transaction

fun createGetAction(): Action {
    return Action(
        Command.GET,
        arrayOf(Disposition.SITTING, Disposition.STANDING, Disposition.FIGHTING),
        arrayOf(Syntax.COMMAND, Syntax.ITEM_IN_ROOM),
        { _: ActionContextService, context: ActionContextList, request: Request ->
            val item = context.getResultBySyntax<ItemEntity>(Syntax.ITEM_IN_ROOM)
            transaction {
                item.inventory = request.mob.inventory
            }
            Response(
                request,
                context,
                Message(
                    "you pick up ${item.name}.",
                    "${request.mob.name} picks up ${item.name}."))
        })
}
