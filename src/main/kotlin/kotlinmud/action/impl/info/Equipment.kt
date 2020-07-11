package kotlinmud.action.impl.info

import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.model.createResponseWithEmptyActionContext
import kotlinmud.item.dao.ItemDAO

fun createEquipmentAction(): Action {
    return Action(Command.EQUIPMENT) {
        val items = it.getMob().equipped.toList()
        createResponseWithEmptyActionContext(
            messageToActionCreator("Your equipment:\n\n${describeEquipment(items)}")
        )
    }
}

fun describeEquipment(items: List<ItemDAO>): String {
    return items.joinToString("\n") { "${it.position.toString().toLowerCase()} - $it" }
}
