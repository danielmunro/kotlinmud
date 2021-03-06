package kotlinmud.action.impl.equipment

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createWearMessage
import kotlinmud.io.factory.equipmentInInventory
import kotlinmud.io.type.Syntax
import kotlinmud.item.model.Item

fun createWearAction(): Action {
    return ActionBuilder(Command.WEAR).also {
        it.syntax = equipmentInInventory()
    } build { svc ->
        val item = svc.get<Item>(Syntax.EQUIPMENT_IN_INVENTORY)
        val mob = svc.getMob()
        val removed = mob.equipped.find { it.position == item.position }
        mob.items.remove(item)
        mob.equipped.add(item)
        svc.createOkResponse(createWearMessage(mob, item, removed))
    }
}
