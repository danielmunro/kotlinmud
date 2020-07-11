package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.io.type.Syntax
import kotlinmud.mob.dao.MobDAO

class EquippedItemContextBuilder(private val mob: MobDAO) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return mob.equipped.find { kotlinmud.helper.string.matches(it.name, word) }?.let {
            Context<Any>(syntax, Status.OK, it)
        } ?: return Context<Any>(
            syntax,
            Status.ERROR,
            "you're not wearing that."
        )
    }
}
