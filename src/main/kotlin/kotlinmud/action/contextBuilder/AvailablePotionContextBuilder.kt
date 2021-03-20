package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.helper.string.matches
import kotlinmud.io.type.Syntax
import kotlinmud.item.type.ItemType
import kotlinmud.mob.model.Mob

class AvailablePotionContextBuilder(private val mob: Mob) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return mob.items.find {
            it.type == ItemType.POTION && word.matches(it.name)
        }?.let {
            Context(syntax, Status.OK, it)
        } ?: Context(syntax, Status.ERROR, "you cannot find that potion.")
    }
}
