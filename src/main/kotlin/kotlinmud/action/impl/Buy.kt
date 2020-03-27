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

fun createBuyAction(): Action {
    return Action(
        Command.BUY,
        mustBeAlert(),
        listOf(Syntax.COMMAND, Syntax.ITEM_FROM_MERCHANT),
        { svc: ActionContextService, request: Request ->
            val item: Item = svc.get(Syntax.ITEM_FROM_MERCHANT)
            val shopkeeper = svc.getMobsInRoom(request.room).find { it.job == JobType.SHOPKEEPER }!!
            shopkeeper.inventory.items.remove(item)
            request.mob.inventory.items.add(item)
            request.mob.gold -= item.worth
            shopkeeper.gold += item.worth
            svc.createResponse(
                Message(
                    "you buy $item from $shopkeeper for ${item.worth} gold.",
                    "${request.mob} buys $item from you.",
                    "${request.mob} buys $item from $shopkeeper."
                )
            )
        }
    )
}
