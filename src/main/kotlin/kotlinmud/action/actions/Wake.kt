package kotlinmud.action.actions

import kotlinmud.action.Action
import kotlinmud.action.ActionContextList
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.mob.Disposition
import org.jetbrains.exposed.sql.transactions.transaction

fun createWakeAction(): Action {
    return Action(
        Command.WAKE,
        listOf(Disposition.SLEEPING, Disposition.SITTING),
        listOf(Syntax.COMMAND),
        { _: ActionContextService, context: ActionContextList, request: Request ->
            transaction { request.mob.disposition = Disposition.STANDING.value }
            Response(
                context,
                Message("you stand up.", "${request.mob.name} stands up.")
            )
        })
}