package kotlinmud.action.actions

import kotlinmud.action.Action
import kotlinmud.action.ActionContextList
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.io.createResponseWithEmptyActionContext
import kotlinmud.mob.Disposition
import kotlinmud.mob.MobEntity
import org.jetbrains.exposed.sql.transactions.transaction

fun createLookAction(): Action {
    return Action(
        Command.LOOK,
        arrayOf(Disposition.SITTING, Disposition.STANDING, Disposition.FIGHTING),
        arrayOf(Syntax.COMMAND),
        { actionContextService: ActionContextService, _: ActionContextList, request: Request ->
            createResponseWithEmptyActionContext(
                Message(describeRoom(request, actionContextService.getMobsInRoom(request.room)))
            )
        })
}

fun describeRoom(request: Request, mobs: List<MobEntity>): String {
    val observers = mobs.filter { it != request.mob }
    return transaction { String.format("%s\n%s\nExits [%s]%s%s%s%s",
        request.room.name,
        request.room.description,
        request.room.exits.joinToString("") { it.direction.name.subSequence(0, 1) },
        if (request.room.inventory.items.count() > 0) "\n" else "",
        request.room.inventory.items.joinToString("\n") { "${it.name} is here." },
        if (observers.count() > 0) "\n" else "",
        observers.joinToString("\n") { "${it.name} is ${it.disposition.toLowerCase()} here." }
    ) }
}
