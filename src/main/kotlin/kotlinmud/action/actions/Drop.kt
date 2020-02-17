package kotlinmud.action.actions

import kotlinmud.action.*
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.item.ItemEntity
import org.jetbrains.exposed.sql.transactions.transaction

fun createDropAction(): Action {
    return Action(
        Command.DROP,
        mustBeAlive(),
        listOf(Syntax.COMMAND, Syntax.ITEM_IN_INVENTORY),
        { _: ActionContextService, context: ActionContextList, request: Request ->
            val item = context.getResultBySyntax<ItemEntity>(Syntax.ITEM_IN_INVENTORY)
            transaction {
                item.inventory = request.room.inventory
            }
            Response(
                context,
                Message(
                    "you drop ${item.name}.",
                    "${request.mob.name} drops ${item.name}."))
        })
}
