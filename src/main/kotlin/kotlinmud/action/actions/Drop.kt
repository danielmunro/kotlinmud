package kotlinmud.action.actions

import kotlinmud.action.*
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.item.ItemEntity
import org.jetbrains.exposed.sql.transactions.transaction

fun createDropAction(): Action {
    return Action(
        Command.DROP,
        mustBeAwake(),
        listOf(Syntax.COMMAND, Syntax.ITEM_IN_INVENTORY),
        { svc: ActionContextService, request: Request ->
            val item = svc.get<ItemEntity>(Syntax.ITEM_IN_INVENTORY)
            transaction { item.inventory = request.room.inventory }
            svc.createResponse(
                Message("you drop ${item.name}.", "${request.mob.name} drops ${item.name}."))
        })
}
