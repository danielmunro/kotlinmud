package kotlinmud.action.impl.info

import kotlinmud.action.Action
import kotlinmud.action.type.Command
import kotlinmud.io.createResponseWithEmptyActionContext
import kotlinmud.io.messageToActionCreator
import kotlinmud.item.Item

fun createEquipmentAction(): Action {
    return Action(Command.EQUIPMENT) {
        val items = it.getMob().equipped.toList()
        createResponseWithEmptyActionContext(
            messageToActionCreator("Your equipment:\n\n${describeEquipment(items)}")
        )
    }
}

fun describeEquipment(items: List<Item>): String {
    return items.joinToString("\n") { "${it.position.toString().toLowerCase()} - $it" }
}
