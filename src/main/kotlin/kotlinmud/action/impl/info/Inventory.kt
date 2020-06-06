package kotlinmud.action.impl.info

import kotlinmud.action.helper.mustBeAwake
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.model.createResponseWithEmptyActionContext
import kotlinmud.item.model.Item

fun createInventoryAction(): Action {
    return Action(Command.INVENTORY, mustBeAwake()) {
        val items = it.getItemsFor(it.getMob())
        createResponseWithEmptyActionContext(
            messageToActionCreator("Your inventory:\n\n${describeItems(items)}")
        )
    }
}

fun describeItems(items: List<Item>): String {
    return items.groupBy { it.id }
        .map { if (it.value.size > 1) "(${it.value.size}) " else "" + it.value[0].name }
        .fold("\n") { acc, it -> "$acc$it\n" }
}
