package kotlinmud.action.impl

import kotlinmud.action.*
import kotlinmud.affect.AffectType
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.io.createResponseWithEmptyActionContext
import kotlinmud.mob.Mob
import kotlinmud.room.exit.DoorDisposition
import kotlinmud.room.exit.Exit

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
    return String.format("%s\n%s\n%sExits [%s]%s%s%s%s",
        request.room.name,
        request.room.description,
        showDoors(request.room.exits),
        reduceExits(request.room.exits),
        if (request.room.inventory.items.count() > 0) "\n" else "",
        request.room.inventory.items.joinToString("\n") { "${it.name} is here." },
        if (observers.count() > 0) "\n" else "",
        observers.joinToString("\n") { "${it.name} is ${it.disposition.value.toLowerCase()} here." }
    )
}

fun showDoors(exits: List<Exit>): String {
    val doors = exits.filter { it.door != null }
        .joinToString("\n") { "${it.door!!.name} to the ${it.direction.value.toLowerCase()} is ${it.door.disposition.toString().toLowerCase()}." }
    if (doors != "") {
        return "\n$doors\n"
    }
    return ""
}

fun reduceExits(exits: List<Exit>): String {
    return exits.filter { it.door == null || it.door.disposition == DoorDisposition.OPEN }
        .joinToString("") { it.direction.name.subSequence(0, 1) }
}
