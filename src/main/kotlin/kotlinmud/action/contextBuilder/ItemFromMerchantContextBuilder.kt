package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.helper.string.matches
import kotlinmud.io.type.Syntax
import kotlinmud.item.service.ItemService
import kotlinmud.mob.model.Mob
import kotlinmud.mob.service.CurrencyService
import kotlinmud.mob.type.JobType

class ItemFromMerchantContextBuilder(
    private val itemService: ItemService,
    private val mob: Mob,
    private val mobsInRoom: List<Mob>
) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        val shopkeeper = mobsInRoom.find { it.job == JobType.SHOPKEEPER }
            ?: return Context(
                Syntax.ITEM_FROM_MERCHANT,
                Status.ERROR,
                "no merchant is here."
            )
        val item = shopkeeper.items.find { word.matches(it.name) }
            ?: return Context(
                Syntax.ITEM_FROM_MERCHANT,
                Status.ERROR,
                "they don't have anything like that."
            )
        val currencyService = CurrencyService(mob)
        if (!currencyService.canAfford(item)) {
            return Context(
                Syntax.ITEM_FROM_MERCHANT,
                Status.ERROR,
                "you can't afford that."
            )
        }
        return Context(Syntax.ITEM_FROM_MERCHANT, Status.OK, item)
    }
}
