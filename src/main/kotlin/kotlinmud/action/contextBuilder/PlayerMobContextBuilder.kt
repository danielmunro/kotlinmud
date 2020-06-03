package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.io.Syntax
import kotlinmud.mob.service.MobService
import kotlinmud.string.matches

class PlayerMobContextBuilder(private val mobService: MobService) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return mobService.getMobRooms().find {
            matches(it.mob.name, word)
        }?.let {
            Context<Any>(syntax, Status.OK, it.mob)
        } ?: Context<Any>(syntax, Status.ERROR, "they aren't here.")
    }
}
