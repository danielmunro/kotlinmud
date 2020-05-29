package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.io.Syntax
import kotlinmud.mob.MobService
import kotlinmud.mob.model.Mob
import kotlinmud.string.matches
import kotlinmud.world.room.Room

class TargetMobContextBuilder(private val mobService: MobService, private val mob: Mob, private val room: Room) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        if (word == "") {
            return mobService.findFightForMob(mob)?.let {
                Context<Any>(
                    syntax,
                    Status.OK,
                    it.getOpponentFor(mob)!!
                )
            }
                ?: notFound(syntax)
        }

        val mobs = mobService.getMobsForRoom(room)
        return mobs.find { matches(it.name, word) }?.let {
            Context<Any>(
                syntax,
                Status.OK,
                it
            )
        }
            ?: notFound(syntax)
    }

    private fun notFound(syntax: Syntax): Context<Any> {
        return Context<Any>(
            syntax,
            Status.FAILED,
            "you don't see them anywhere."
        )
    }
}
