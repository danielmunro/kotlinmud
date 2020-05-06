package kotlinmud.action.impl.info

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Message
import kotlinmud.io.createResponseWithEmptyActionContext
import kotlinmud.item.Item

fun createEquipmentAction(): Action {
    return Action(Command.EQUIPMENT, mustBeAlert()) {
        val items = it.getMob().equipped.toList()
        createResponseWithEmptyActionContext(
            Message("Your equipment:\n\n${describeEquipment(items)}")
        )
    }
}

fun describeEquipment(items: List<Item>): String {
    return items.joinToString("\n") { "${it.position.toString().toLowerCase()} - $it" }
}
