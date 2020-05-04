package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Message
import kotlinmud.io.Syntax
import kotlinmud.item.Item
import kotlinmud.mob.JobType

fun createSellAction(): Action {
    return Action(
        Command.SELL,
        mustBeAlert(),
        listOf(Syntax.COMMAND, Syntax.ITEM_IN_INVENTORY)) {
            val item: Item = it.get(Syntax.ITEM_IN_INVENTORY)
            val shopkeeper = it.getMobsInRoom().find { mob -> mob.job == JobType.SHOPKEEPER }!!
            it.changeItemOwner(item, shopkeeper)
            it.addGold(item.worth)
            shopkeeper.gold -= item.worth
            it.createResponse(
                Message(
                    "you sell $item to $shopkeeper for ${item.worth} gold.",
                    "${it.getMob()} sells $item to you.",
                    "${it.getMob()} sells $item to $shopkeeper."
                )
            )
        }
}
