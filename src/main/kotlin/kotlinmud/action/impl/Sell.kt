package kotlinmud.action.impl

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.itemInInventory
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.item.dao.ItemDAO
import kotlinmud.mob.type.JobType

fun createSellAction(): Action {
    return Action(Command.SELL, mustBeAlert(), itemInInventory()) {
        val item = it.get<ItemDAO>(Syntax.ITEM_IN_INVENTORY)
        val shopkeeper = it.getMobsInRoom().find { mob -> mob.job == JobType.SHOPKEEPER }!!
        it.giveItemToMob(item, shopkeeper)
        it.transferGold(shopkeeper, it.getMob(), item.worth)
        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("you sell $item to $shopkeeper for ${item.worth} gold.")
                .toTarget("${it.getMob()} sells $item to you.")
                .toObservers("${it.getMob()} sells $item to $shopkeeper.")
                .build()
        )
    }
}
