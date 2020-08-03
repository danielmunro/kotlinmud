package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.helper.string.matches
import kotlinmud.io.type.Syntax
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.HasInventory
import kotlinmud.mob.dao.MobDAO
import kotlinmud.room.dao.RoomDAO
import org.jetbrains.exposed.sql.transactions.transaction

class AvailableItemInventoryContextBuilder(
    private val mob: MobDAO,
    private val room: RoomDAO,
    private val itemService: ItemService
) : ContextBuilder {
    companion object {
        fun isMatch(item: ItemDAO, word: String): Boolean {
            return matches(item.name, word)
        }
    }

    override fun build(syntax: Syntax, word: String): Context<Any> {
        return tryInventory(mob, syntax, word)
            ?: tryInventory(room, syntax, word)
            ?: Context<Any>(
                syntax,
                Status.ERROR,
                "you don't see that anywhere."
            )
    }

    private fun tryInventory(hasInventory: HasInventory, syntax: Syntax, word: String): Context<Any>? {
        return itemService.findAllByOwner(hasInventory).find {
            isMatch(it, word) && it.isContainer
        }?.let {
            transaction {
//                checkWeight(syntax, it) ?:
                checkItemCount(syntax, it) ?: Context<Any>(syntax, Status.OK, it)
            }
        }
    }

    private fun checkItemCount(syntax: Syntax, inventory: ItemDAO): Context<Any>? {
        val count = inventory.items.count()
        return if (inventory.maxItems == null || count + 1 <= inventory.maxItems!!)
            null
        else
            Context<Any>(syntax, Status.ERROR, "that is full.")
    }

//    private fun checkWeight(syntax: Syntax, inventory: ItemDAO): Context<Any>? {
//        val weight = inventory.items.fold(0.0) { acc, it -> acc + it.weight }
//        return if (inventory.maxWeight == null || weight + item.weight < inventory.maxWeight!!)
//            null
//        else
//            Context<Any>(syntax, Status.ERROR, "that is too heavy.")
//    }
}
