package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.helper.string.matches
import kotlinmud.io.type.Syntax
import kotlinmud.mob.model.Mob

class EquipmentInInventoryContextBuilder(private val mob: Mob) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        val item = mob.items.find { word.matches(it.name) }
            ?: return Context<Any>(
                syntax,
                Status.ERROR,
                "you don't have that."
            )
        if (item.position == null) {
            return Context<Any>(syntax, Status.ERROR, "you can't equip that.")
        }
        return Context<Any>(syntax, Status.OK, item)
    }
}
