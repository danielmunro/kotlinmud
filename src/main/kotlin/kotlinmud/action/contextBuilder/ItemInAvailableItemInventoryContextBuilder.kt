package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.helper.string.matches
import kotlinmud.io.type.Syntax
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.HasInventory

class ItemInAvailableItemInventoryContextBuilder(
    private val itemService: ItemService,
    private val hasInventory: HasInventory
) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return itemService.findAllByOwner(hasInventory).find {
            word.matches(it.name)
        }?.let {
            Context<Any>(syntax, Status.OK, it)
        } ?: Context<Any>(
            syntax,
            Status.ERROR,
            "you can't find that anywhere."
        )
    }
}
