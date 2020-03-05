package kotlinmud.action.contextBuilder

import kotlinmud.action.Context
import kotlinmud.action.Status
import kotlinmud.io.Syntax
import kotlinmud.item.Position
import kotlinmud.mob.Mob

class EquipmentInInventoryContextBuilder(private val mob: Mob) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        val item = mob.inventory.items.find { kotlinmud.string.matches(it.name, word) }
            ?: return Context<Any>(syntax, Status.ERROR, "you don't have that.")
        if (item.position == Position.NONE) {
            return Context<Any>(syntax, Status.ERROR, "you can't equip that.")
        }
        return Context<Any>(syntax, Status.OK, item)
    }
}