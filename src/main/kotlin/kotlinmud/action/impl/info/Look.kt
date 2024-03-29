package kotlinmud.action.impl.info

import kotlinmud.action.helper.mustBeAwake
import kotlinmud.action.model.Action
import kotlinmud.action.service.ActionContextService
import kotlinmud.action.type.Command
import kotlinmud.affect.type.AffectType
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.model.createResponseWithEmptyActionContext
import kotlinmud.item.model.Item
import kotlinmud.mob.model.Mob
import kotlinmud.room.model.Room
import kotlinmud.room.type.DoorDisposition

fun createLookAction(): Action {
    return Action(Command.LOOK, mustBeAwake()) {
        val room = it.getRoom()
        createResponseWithEmptyActionContext(
            messageToActionCreator(
                describeRoom(
                    it,
                    room,
                    it.getMob(),
                    it.getMobsInRoom(),
                    room.items,
                )
            )
        )
    }
}

fun describeRoom(actionContextService: ActionContextService, room: Room, mob: Mob, mobs: List<Mob>, roomItems: List<Item>): String {
    mob.affects.find { it.type == AffectType.BLIND }?.let {
        return "you can't see anything, you're blind!"
    }
    val observers = mobs.filter {
        it != mob && it.affects.find { affect -> affect.type == AffectType.INVISIBILITY } == null
    }
    return String.format(
        "(%d) %s\n%sExits [%s]%s%s%s%s",
        room.id,
        actionContextService.getDynamicRoomDescription(),
        showDoors(room),
        reduceExits(room),
        if (roomItems.isNotEmpty()) "\n" else "",
        roomItems.joinToString("\n") { "${it.name} is here." },
        if (observers.count() > 0) "\n" else "",
        observers.joinToString("\n") { it.brief }
    )
}

fun showDoors(room: Room): String {
    val doors = room.getDoors()
        .entries
        .joinToString("\n") { "${it.value.name} to the ${it.key.value.lowercase()} is ${it.value.disposition.toString().lowercase()}." }
    if (doors != "") {
        return "\n$doors\n"
    }
    return ""
}

fun reduceExits(room: Room): String {
    val doors = room.getDoors()
    return room.getAllExits()
        .entries
        .filter {
            doors[it.key] == null || doors[it.key]?.disposition == DoorDisposition.OPEN
        }
        .joinToString("") { it.key.name.subSequence(0, 1) }
}
