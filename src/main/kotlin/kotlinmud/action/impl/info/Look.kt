package kotlinmud.action.impl.info

import kotlinmud.action.helper.mustBeAwake
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.affect.type.AffectType
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.model.createResponseWithEmptyActionContext
import kotlinmud.item.dao.ItemDAO
import kotlinmud.mob.dao.MobDAO
import kotlinmud.room.dao.RoomDAO
import org.jetbrains.exposed.sql.transactions.transaction

fun createLookAction(): Action {
    return Action(Command.LOOK, mustBeAwake()) {
        val room = it.getRoom()
        createResponseWithEmptyActionContext(
            messageToActionCreator(
                describeRoom(
                    room,
                    it.getMob(),
                    it.getMobsInRoom(),
                    it.getItemsFor(room)
                )
            )
        )
    }
}

fun describeRoom(room: RoomDAO, mob: MobDAO, mobs: List<MobDAO>, roomItems: List<ItemDAO>): String {
    return transaction {
        mob.affects.find { it.type == AffectType.BLIND }?.let {
            return@transaction "you can't see anything, you're blind!"
        }
        val observers = mobs.filter {
            it != mob && it.affects.find { affect -> affect.type == AffectType.INVISIBILITY } == null
        }
        return@transaction String.format("%s\n%s\n%sExits [%s]%s%s%s%s",
            room.name,
            room.description,
            showDoors(room),
            reduceExits(room),
            if (roomItems.isNotEmpty()) "\n" else "",
            roomItems.joinToString("\n") { "${it.name} is here." },
            if (observers.count() > 0) "\n" else "",
            observers.joinToString("\n") { it.brief }
        )
    }
}

fun showDoors(room: RoomDAO): String {
    val doors = room.getDoors()
        .entries
        .joinToString("\n") { "${it.value.name} to the ${it.key.value.toLowerCase()} is ${it.value.disposition.toString().toLowerCase()}." }
    if (doors != "") {
        return "\n$doors\n"
    }
    return ""
}

fun reduceExits(room: RoomDAO): String {
    return room.getAllExits()
        .entries
        .joinToString("") { it.key.name.subSequence(0, 1) }
}
