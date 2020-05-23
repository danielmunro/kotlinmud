package kotlinmud.action.contextBuilder

import kotlinmud.action.Context
import kotlinmud.action.Status
import kotlinmud.io.Syntax
import kotlinmud.item.HasInventory
import kotlinmud.item.ItemService
import kotlinmud.string.matches

class ItemInAvailableItemInventoryContextBuilder(
    private val itemService: ItemService,
    private val hasInventory: HasInventory
) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return itemService.findAllByOwner(hasInventory).find {
            matches(it.name, word)
        }?.let {
            Context<Any>(syntax, Status.OK, it)
        } ?: Context<Any>(syntax, Status.ERROR, "you can't find that anywhere.")
    }
}
