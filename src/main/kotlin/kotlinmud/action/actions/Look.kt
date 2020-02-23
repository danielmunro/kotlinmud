package kotlinmud.action.actions

import kotlinmud.action.*
import kotlinmud.affect.AffectType
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.io.createResponseWithEmptyActionContext
import kotlinmud.mob.Mob

fun createLookAction(): Action {
    return Action(
        Command.LOOK,
        mustBeAwake(),
        listOf(Syntax.COMMAND),
        { svc: ActionContextService, request: Request ->
            createResponseWithEmptyActionContext(
                Message(describeRoom(request, svc.getMobsInRoom(request.room))))
        })
}

fun describeRoom(request: Request, mobs: List<Mob>): String {
    request.mob.affects.find {
        it.affectType == AffectType.BLIND
    }?.let { return "you can't see anything, you're blind!" }
    val observers = mobs.filter { it != request.mob }
    return String.format("%s\n%s\nExits [%s]%s%s%s%s",
        request.room.name,
        request.room.description,
        request.room.exits.joinToString("") { it.direction.name.subSequence(0, 1) },
        if (request.room.inventory.items.count() > 0) "\n" else "",
        request.room.inventory.items.joinToString("\n") { "${it.name} is here." },
        if (observers.count() > 0) "\n" else "",
        observers.joinToString("\n") { "${it.name} is ${it.disposition.value.toLowerCase()} here." }
    )
}
