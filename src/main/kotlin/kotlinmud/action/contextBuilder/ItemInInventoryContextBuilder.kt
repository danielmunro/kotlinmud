package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.io.type.Syntax
import kotlinmud.item.service.ItemService
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.model.Mob

class ItemInInventoryContextBuilder(private val itemService: ItemService, private val mob: MobDAO) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return itemService.findByOwner(mob, word)
                ?.let { Context<Any>(syntax, Status.OK, it) }
            ?: Context<Any>(syntax, Status.FAILED, "you don't have that.")
    }
}
