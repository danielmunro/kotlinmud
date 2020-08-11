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
        val removed = transaction {
            svc.getMob().equipped.find {
                it.position == item.position
            }?.let {
                it.mobEquipped = null
                it
            }
        }
        transaction { item.mobEquipped = svc.getMob() }
        svc.createOkResponse(createWearMessage(svc.getMob(), item, removed))
    }
}
