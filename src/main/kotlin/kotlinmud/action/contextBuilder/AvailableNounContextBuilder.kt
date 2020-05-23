package kotlinmud.action.contextBuilder

import kotlinmud.action.Context
import kotlinmud.action.Status
import kotlinmud.affect.AffectType
import kotlinmud.io.Syntax
import kotlinmud.item.ItemService
import kotlinmud.mob.Mob
import kotlinmud.mob.MobService
import kotlinmud.string.matches
import kotlinmud.world.room.Room

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
            ?: itemService.findByOwner(room, word)
            ?: return Context(syntax, Status.FAILED, "you don't see anything like that here.")
        return Context(syntax, Status.OK, target)
    }
}
