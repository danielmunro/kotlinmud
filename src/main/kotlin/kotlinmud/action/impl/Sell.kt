package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.MessageBuilder
import kotlinmud.io.Syntax
import kotlinmud.io.itemInInventory
import kotlinmud.item.Item
import kotlinmud.mob.type.JobType

fun createSellAction(): Action {
    return Action(Command.SELL, mustBeAlert(), itemInInventory()) {
        val item: Item = it.get(Syntax.ITEM_IN_INVENTORY)
        val shopkeeper = it.getMobsInRoom().find { mob -> mob.job == JobType.SHOPKEEPER }!!
        it.changeItemOwner(item, shopkeeper)
        it.addGold(item.worth)
        shopkeeper.gold -= item.worth
        it.createResponse(
            MessageBuilder()
                .toActionCreator("you sell $item to $shopkeeper for ${item.worth} gold.")
                .toTarget("${it.getMob()} sells $item to you.")
                .toObservers("${it.getMob()} sells $item to $shopkeeper.")
                .build()
        )
    }
}
