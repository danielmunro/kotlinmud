package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.io.type.Syntax
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.Position
import kotlinmud.mob.dao.MobDAO

class EquipmentInInventoryContextBuilder(private val itemService: ItemService, private val mob: MobDAO) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        val item = itemService.findByOwner(mob, word)
            ?: return Context<Any>(
                syntax,
                Status.ERROR,
                "you don't have that."
            )
        if (item.position == Position.NONE) {
            return Context<Any>(syntax, Status.ERROR, "you can't equip that.")
        }
        return Context<Any>(syntax, Status.OK, item)
    }
}
