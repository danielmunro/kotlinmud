package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.helper.string.matches
import kotlinmud.io.type.Syntax
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.HasInventory
import org.jetbrains.exposed.sql.transactions.transaction

class ItemInAvailableItemInventoryContextBuilder(
    private val hasInventory: Any
) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        val items = if (hasInventory is ItemDAO) {
            transaction { hasInventory.items.toList() }
        } else if (hasInventory is HasInventory) {
            hasInventory.items
        } else {
            throw Exception()
        }
        return items.find {
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
