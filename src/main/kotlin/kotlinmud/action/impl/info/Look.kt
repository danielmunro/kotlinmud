package kotlinmud.action.impl.info

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAwake
import kotlinmud.affect.AffectType
import kotlinmud.io.Message
import kotlinmud.io.Syntax
import kotlinmud.io.createResponseWithEmptyActionContext
import kotlinmud.item.Item
import kotlinmud.mob.Mob
import kotlinmud.world.room.Room
import kotlinmud.world.room.exit.DoorDisposition
import kotlinmud.world.room.exit.Exit

fun createLookAction(): Action {
    return Action(
        Command.LOOK,
        mustBeAwake(),
        listOf(Syntax.COMMAND)) {
            val room = it.getRoom()
            createResponseWithEmptyActionContext(
                Message(
                    describeRoom(
                        room,
                        it.getMob(),
                        it.getMobsInRoom(),
                        it.getItemsFor(room)
                    )
                ))
        }
}

fun describeRoom(room: Room, mob: Mob, mobs: List<Mob>, roomItems: List<Item>): String {
    mob.affects().findByType(AffectType.BLIND)?.let {
        return "you can't see anything, you're blind!"
    }
    val observers = mobs.filter {
        it != mob && it.affects().findByType(AffectType.INVISIBILITY) == null
    }
    return String.format("%s\n%s\n%sExits [%s]%s%s%s%s",
        room.name,
        room.description,
        showDoors(room.exits),
        reduceExits(room.exits),
        if (roomItems.isNotEmpty()) "\n" else "",
        roomItems.joinToString("\n") { "${it.name} is here." },
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
