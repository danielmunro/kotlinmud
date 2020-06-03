package kotlinmud.action.impl

import kotlinmud.action.model.Action
import kotlinmud.action.mustBeAlert
import kotlinmud.action.type.Command
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.factory.itemFromMerchant
import kotlinmud.io.type.Syntax
import kotlinmud.item.model.Item
import kotlinmud.mob.type.JobType

fun createBuyAction(): Action {
    return Action(Command.BUY, mustBeAlert(), itemFromMerchant()) {
        val item: Item = it.get(Syntax.ITEM_FROM_MERCHANT)
        val shopkeeper = it.getMobsInRoom().find { mob -> mob.job == JobType.SHOPKEEPER }!!
        it.changeItemOwner(item, it.getMob())
        it.deductGold(item.worth)
        shopkeeper.gold += item.worth
        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("you buy $item from $shopkeeper for ${item.worth} gold.")
                .toTarget("${it.getMob()} buys $item from you.")
                .toObservers("${it.getMob()} buys $item from $shopkeeper.")
                .build()
        )
    }
}
