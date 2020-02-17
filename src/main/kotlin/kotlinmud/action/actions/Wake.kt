package kotlinmud.action.actions

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.mob.Disposition
import org.jetbrains.exposed.sql.transactions.transaction

fun createWakeAction(): Action {
    return Action(
        Command.WAKE,
        listOf(Disposition.SLEEPING, Disposition.SITTING),
        listOf(Syntax.COMMAND),
        { svc: ActionContextService, request: Request ->
            transaction { request.mob.disposition = Disposition.STANDING.value }
            svc.createResponse(
                Message("you stand up.", "${request.mob.name} stands up."))
        })
}
