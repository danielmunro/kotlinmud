package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.helper.string.matches
import kotlinmud.io.type.Syntax
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.model.Mob
import kotlinmud.mob.service.MobService
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.model.Room

class TargetMobContextBuilder(private val mobService: MobService, private val mob: MobDAO, private val room: RoomDAO) : ContextBuilder {
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
