package kotlinmud.action.actions

import kotlinmud.action.*
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.item.ItemEntity
import org.jetbrains.exposed.sql.transactions.transaction

fun createGetAction(): Action {
    return Action(
        Command.GET,
        mustBeAwake(),
        listOf(Syntax.COMMAND, Syntax.ITEM_IN_ROOM),
        { svc: ActionContextService, request: Request ->
            val item = svc.get<ItemEntity>(Syntax.ITEM_IN_ROOM)
            transaction { item.inventory = request.mob.inventory }
            svc.createResponse(Message(
                    "you pick up ${item.name}.",
                    "${request.mob.name} picks up ${item.name}."))
        })
}
