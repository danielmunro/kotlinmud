package kotlinmud.action

import kotlinmud.EventService
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
        { _: EventService, _: ContextCollection, request: Request -> Response(request, describeRoom(request.room)) })
}

fun describeRoom(room: Room): String {
    return transaction { String.format("%s\n%s\nExits [%s]",
        room.name,
        room.description,
        room.exits.joinToString { it.direction.name.subSequence(0, 1) }
    ) }
}
