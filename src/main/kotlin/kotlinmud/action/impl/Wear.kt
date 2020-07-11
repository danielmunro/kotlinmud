package kotlinmud.action.impl

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.equipmentInInventory
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.item.model.Item

fun createWearAction(): Action {
    return Action(
        Command.WEAR,
        mustBeAlert(),
        equipmentInInventory()
    ) { svc ->
        val item = svc.get<Item>(Syntax.EQUIPMENT_IN_INVENTORY)
        val removed = svc.getMob().equipped.find {
            it.position == item.position
        }?.let {
            svc.getMob().equipped.minus(it)
            it
        }
        svc.getMob().equipped.plus(item)
        svc.createOkResponse(
            MessageBuilder()
                .toActionCreator("you ${if (removed != null) "remove $removed and " else ""}wear $item.")
                .toObservers("${svc.getMob()} ${if (removed != null) "removes $removed and " else ""}wears $item.")
                .build()
        )
    }
}
