package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeAwake
import kotlinmud.affect.AffectType
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.io.createResponseWithEmptyActionContext
import kotlinmud.mob.Mob
import kotlinmud.room.Room
import kotlinmud.room.exit.DoorDisposition
import kotlinmud.room.exit.Exit

fun createLookAction(): Action {
    return Action(
        Command.LOOK,
        mustBeAwake(),
        listOf(Syntax.COMMAND),
        { svc: ActionContextService, request: Request ->
            createResponseWithEmptyActionContext(
                Message(describeRoom(request.room, request.mob, svc.getMobsInRoom(request.room))))
        })
}

fun describeRoom(room: Room, mob: Mob, mobs: List<Mob>): String {
    mob.affects.find {
        it.affectType == AffectType.BLIND
    }?.let { return "you can't see anything, you're blind!" }
    val observers = mobs.filter {
        it != mob && !it.isAffectedBy(AffectType.INVISIBLE)
    }
    return String.format("%s\n%s\n%sExits [%s]%s%s%s%s",
        room.name,
        room.description,
        showDoors(room.exits),
        reduceExits(room.exits),
        if (room.inventory.items.count() > 0) "\n" else "",
        room.inventory.items.joinToString("\n") { "${it.name} is here." },
        if (observers.count() > 0) "\n" else "",
        observers.joinToString("\n") { it.brief }
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
