package kotlinmud.action.actions

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.mob.Disposition
import org.jetbrains.exposed.sql.transactions.transaction

fun createSleepAction(): Action {
    return Action(
        Command.SLEEP,
        listOf(Disposition.STANDING, Disposition.SITTING),
        listOf(Syntax.COMMAND),
        { svc: ActionContextService, request: Request ->
            transaction { request.mob.disposition = Disposition.SLEEPING.value }
            svc.createResponse(
                Message("you lay down and go to sleep.", "${request.mob.name} lays down and goes to sleep.")
            )
        })
}
