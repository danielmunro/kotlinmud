package kotlinmud.action.impl.shop

import kotlinmud.action.exception.InvokeException
import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createSellMessage
import kotlinmud.io.factory.itemToSell
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.type.Syntax
import kotlinmud.item.dao.ItemDAO
import kotlinmud.mob.service.CurrencyService
import kotlinmud.mob.type.JobType

fun createSellAction(): Action {
    return Action(Command.SELL, mustBeAlert(), itemToSell()) {
        val item = it.get<ItemDAO>(Syntax.ITEM_TO_SELL)
        val shopkeeper = it.getMobsInRoom().find { mob -> mob.job == JobType.SHOPKEEPER }!!
        try {
            it.giveItemToMob(item, shopkeeper)
        } catch (e: InvokeException) {
            return@Action it.createErrorResponse(messageToActionCreator("they cannot carry any more."))
        }
        CurrencyService(shopkeeper).transferTo(it.getMob(), item.worth)
        it.createOkResponse(createSellMessage(it.getMob(), shopkeeper, item))
    }
}
