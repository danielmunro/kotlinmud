package kotlinmud.action.impl.shop

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createBuyMessage
import kotlinmud.io.factory.itemFromMerchant
import kotlinmud.io.type.Syntax
import kotlinmud.item.model.Item
import kotlinmud.mob.service.CurrencyService
import kotlinmud.mob.type.JobType

fun createBuyAction(): Action {
    return ActionBuilder(Command.BUY).also {
        it.syntax = itemFromMerchant()
    } build {
        val item = it.get<Item>(Syntax.ITEM_FROM_MERCHANT)
        val shopkeeper = it.getMobsInRoom().find { mob -> mob.job == JobType.SHOPKEEPER }!!
        it.giveItemToMob(item, it.getMob())
        CurrencyService(it.getMob()).transferTo(shopkeeper, item.worth)
        it.createOkResponse(createBuyMessage(it.getMob(), shopkeeper, item))
    }
}
