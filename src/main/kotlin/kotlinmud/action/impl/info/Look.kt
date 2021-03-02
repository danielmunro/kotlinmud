package kotlinmud.action.impl.info

import kotlinmud.action.helper.mustBeAwake
import kotlinmud.action.model.Action
import kotlinmud.action.service.ActionContextService
import kotlinmud.action.type.Command
import kotlinmud.affect.type.AffectType
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.model.createResponseWithEmptyActionContext
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.model.Item
import kotlinmud.mob.model.Mob
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.model.Room
import org.jetbrains.exposed.sql.transactions.transaction

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
    return transaction {
        mob.affects.find { it.type == AffectType.BLIND }?.let {
            return@transaction "you can't see anything, you're blind!"
        }
        val observers = mobs.filter {
            it != mob && it.affects.find { affect -> affect.type == AffectType.INVISIBILITY } == null
        }
        return@transaction String.format(
            "%s\n%sExits [%s]%s%s%s%s",
            actionContextService.getDynamicRoomDescription(),
            showDoors(room),
            reduceExits(room),
            if (roomItems.isNotEmpty()) "\n" else "",
            roomItems.joinToString("\n") { "${it.name} is here." },
            if (observers.count() > 0) "\n" else "",
            observers.joinToString("\n") { it.brief }
        )
    }
}

fun showDoors(room: Room): String {
    val doors = room.getDoors()
        .entries
        .joinToString("\n") { "${it.value.name} to the ${it.key.value.toLowerCase()} is ${it.value.disposition.toString().toLowerCase()}." }
    if (doors != "") {
        return "\n$doors\n"
    }
    return ""
}

fun reduceExits(room: Room): String {
    return room.getAllExits()
        .entries
        .joinToString("") { it.key.name.subSequence(0, 1) }
}
