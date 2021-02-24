package kotlinmud.action.impl.equipment

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createWearMessage
import kotlinmud.io.factory.equipmentInInventory
import kotlinmud.io.type.Syntax
import kotlinmud.item.dao.ItemDAO
import org.jetbrains.exposed.sql.transactions.transaction

fun createWearAction(): Action {
    return Action(
        Command.WEAR,
        mustBeAlert(),
        equipmentInInventory()
    ) { svc ->
        val item = svc.get<ItemDAO>(Syntax.EQUIPMENT_IN_INVENTORY)
        val mob = svc.getMob()
        val removed = transaction {
            mob.equipped.find {
                it.position == item.position
            }
        }
        mob.items.remove(item)
        mob.equipped.add(item)
        svc.createOkResponse(createWearMessage(mob, item, removed))
    }
}
