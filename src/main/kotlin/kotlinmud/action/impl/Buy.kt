package kotlinmud.action.impl

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createBuyMessage
import kotlinmud.io.factory.itemFromMerchant
import kotlinmud.io.type.Syntax
import kotlinmud.item.dao.ItemDAO
import kotlinmud.mob.type.JobType

fun createBuyAction(): Action {
    return Action(Command.BUY, mustBeAlert(), itemFromMerchant()) {
        val item = it.get<ItemDAO>(Syntax.ITEM_FROM_MERCHANT)
        val shopkeeper = it.getMobsInRoom().find { mob -> mob.job == JobType.SHOPKEEPER }!!
        it.giveItemToMob(item, it.getMob())
        it.transferGold(it.getMob(), shopkeeper, item.worth)
        it.createOkResponse(createBuyMessage(it.getMob(), shopkeeper, item))
    }
}
