package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.item.Item
import kotlinmud.mob.JobType

fun createSellAction(): Action {
    return Action(
        Command.SELL,
        mustBeAlert(),
        listOf(Syntax.COMMAND, Syntax.ITEM_IN_INVENTORY),
        { svc: ActionContextService, request: Request ->
            val item: Item = svc.get(Syntax.ITEM_IN_INVENTORY)
            val shopkeeper = svc.getMobsInRoom(request.room).find { it.job == JobType.SHOPKEEPER }!!
            svc.changeItemOwner(item, shopkeeper)
            request.mob.gold += item.worth
            shopkeeper.gold -= item.worth
            svc.createResponse(
                Message(
                    "you sell $item to $shopkeeper for ${item.worth} gold.",
                    "${request.mob} sells $item to you.",
                    "${request.mob} sells $item to $shopkeeper."
                )
            )
        }
    )
}
