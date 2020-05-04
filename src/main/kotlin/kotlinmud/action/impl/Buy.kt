package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Message
import kotlinmud.io.Syntax
import kotlinmud.item.Item
import kotlinmud.mob.JobType

fun createBuyAction(): Action {
    return Action(
        Command.BUY,
        mustBeAlert(),
        listOf(Syntax.COMMAND, Syntax.ITEM_FROM_MERCHANT)) {
            val item: Item = it.get(Syntax.ITEM_FROM_MERCHANT)
            val shopkeeper = it.getMobsInRoom().find { mob -> mob.job == JobType.SHOPKEEPER }!!
            it.changeItemOwner(item, it.getMob())
            it.getMob().gold -= item.worth
            shopkeeper.gold += item.worth
            it.createResponse(
                Message(
                    "you buy $item from $shopkeeper for ${item.worth} gold.",
                    "${it.getMob()} buys $item from you.",
                    "${it.getMob()} buys $item from $shopkeeper."
                )
            )
        }
}
