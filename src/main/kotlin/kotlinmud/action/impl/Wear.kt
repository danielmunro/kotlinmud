package kotlinmud.action.impl

import kotlinmud.action.model.Action
import kotlinmud.action.mustBeAlert
import kotlinmud.action.type.Command
import kotlinmud.io.MessageBuilder
import kotlinmud.io.Syntax
import kotlinmud.io.equipmentInInventory
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
            svc.getMob().equipped.remove(it)
            it
        }
        svc.getMob().equipped.add(item)
        svc.createOkResponse(
            MessageBuilder()
                .toActionCreator("you ${if (removed != null) "remove $removed and " else ""}wear $item.")
                .toObservers("${svc.getMob()} ${if (removed != null) "removes $removed and " else ""}wears $item.")
                .build()
        )
    }
}
