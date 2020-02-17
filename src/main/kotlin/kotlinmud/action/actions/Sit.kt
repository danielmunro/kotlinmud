package kotlinmud.action.actions

import kotlinmud.action.*
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.mob.Disposition
import org.jetbrains.exposed.sql.transactions.transaction

fun createSitAction(): Action {
    return Action(
        Command.SIT,
        listOf(Disposition.SLEEPING, Disposition.STANDING),
        listOf(Syntax.COMMAND),
        { svc: ActionContextService, request: Request ->
            transaction { request.mob.disposition = Disposition.SITTING.value }
            svc.createResponse(
                Message("you sit down.", "${request.mob.name} sits down."))
        })
}
