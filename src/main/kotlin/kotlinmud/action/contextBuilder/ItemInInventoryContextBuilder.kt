package kotlinmud.action.contextBuilder

import kotlinmud.action.Context
import kotlinmud.action.Status
import kotlinmud.io.Syntax
import kotlinmud.mob.Mob
import org.jetbrains.exposed.sql.transactions.transaction

class ItemInInventoryContextBuilder(private val mob: Mob) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return transaction {
            mob.inventory.items.find { kotlinmud.string.matches(it.name, word) }
                ?.let { Context<Any>(syntax, Status.OK, it) }
            ?: Context<Any>(syntax, Status.FAILED, "you don't have that.")
        }
    }
}
