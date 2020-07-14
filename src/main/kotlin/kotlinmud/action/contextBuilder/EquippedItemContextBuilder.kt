package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.io.type.Syntax
import kotlinmud.mob.dao.MobDAO
import org.jetbrains.exposed.sql.transactions.transaction

class EquippedItemContextBuilder(private val mob: MobDAO) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return transaction {
            mob.equipped.find { kotlinmud.helper.string.matches(it.name, word) }?.let {
                Context<Any>(syntax, Status.OK, it)
            } ?: Context<Any>(
                syntax,
                Status.ERROR,
                "you're not wearing that."
            )
        }
    }
}
