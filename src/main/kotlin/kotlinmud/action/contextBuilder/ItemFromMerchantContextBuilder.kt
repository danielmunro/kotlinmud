package kotlinmud.action.contextBuilder

import kotlinmud.action.Context
import kotlinmud.action.Status
import kotlinmud.io.Syntax
import kotlinmud.mob.JobType
import kotlinmud.mob.Mob
import kotlinmud.service.ItemService

class ItemFromMerchantContextBuilder(
    private val itemService: ItemService,
    private val mob: Mob,
    private val mobsInRoom: List<Mob>
) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        val shopkeeper = mobsInRoom.find { it.job == JobType.SHOPKEEPER }
            ?: return Context(Syntax.ITEM_FROM_MERCHANT, Status.ERROR, "no merchant is here.")
        val item = itemService.findByOwner(shopkeeper, word)
            ?: return Context(Syntax.ITEM_FROM_MERCHANT, Status.ERROR, "they don't have anything like that.")
        if (item.worth > mob.gold) {
            return Context(Syntax.ITEM_FROM_MERCHANT, Status.ERROR, "you can't afford that.")
        }
        return Context(Syntax.ITEM_FROM_MERCHANT, Status.OK, item)
    }
}
