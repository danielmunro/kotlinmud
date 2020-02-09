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
import kotlinmud.mob.Mob
import kotlinmud.room.Room
import org.jetbrains.exposed.sql.transactions.transaction

fun createLookAction(): Action {
    return Action(
        Command.LOOK,
        arrayOf(Disposition.SITTING, Disposition.STANDING, Disposition.FIGHTING),
        arrayOf(Syntax.COMMAND),
        { actionContextService: ActionContextService, _: ContextCollection, request: Request ->
            Response(
                request,
                describeRoom(request, actionContextService.getMobsInRoom(request.room)))
        })
}

fun describeRoom(request: Request, mobs: List<Mob>): String {
    val observers = mobs.filter { it != request.mob }
    return transaction { String.format("%s\n%s\nExits [%s]\n%s",
        request.room.name,
        request.room.description,
        request.room.exits.joinToString("") { it.direction.name.subSequence(0, 1) },
        observers.joinToString("\n") { it.name + " is ${it.disposition.toString().toLowerCase()} here." }
    ) }
}
