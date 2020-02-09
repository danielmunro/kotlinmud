package kotlinmud.action.actions

import kotlinmud.EventService
import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.ContextCollection
import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.mob.Disposition
import kotlinmud.room.Room
import org.jetbrains.exposed.sql.transactions.transaction

fun createLookAction(): Action {
    return Action(
        Command.LOOK,
        arrayOf(Disposition.SITTING, Disposition.STANDING, Disposition.FIGHTING),
        arrayOf(Syntax.COMMAND),
        { _: ActionContextService, _: ContextCollection, request: Request ->
            Response(
                request,
                describeRoom(request.room)
            )
        })
}

private fun describeRoom(room: Room): String {
    return transaction { String.format("%s\n%s\nExits [%s]",
        room.name,
        room.description,
        room.exits.joinToString("") { it.direction.name.subSequence(0, 1) }
    ) }
}
