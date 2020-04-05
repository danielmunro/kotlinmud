package kotlinmud.action.contextBuilder

import kotlinmud.action.Context
import kotlinmud.action.Status
import kotlinmud.affect.AffectType
import kotlinmud.io.Syntax
import kotlinmud.mob.Mob
import kotlinmud.room.Room
import kotlinmud.service.ItemService
import kotlinmud.service.MobService
import kotlinmud.string.matches

class AvailableNounContextBuilder(
    private val mobService: MobService,
    private val itemService: ItemService,
    private val mob: Mob,
    private val room: Room
) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        val target = mobService.getMobsForRoom(room).find {
                matches(it.name, word) && it.affects().findByType(AffectType.INVISIBILITY) == null
            } ?: itemService.findByOwner(mob, word)
            ?: room.inventory.items.find {
                matches(it.name, word)
            } ?: return Context(syntax, Status.FAILED, "you don't see anything like that here.")
        return Context(syntax, Status.OK, target)
    }
}
