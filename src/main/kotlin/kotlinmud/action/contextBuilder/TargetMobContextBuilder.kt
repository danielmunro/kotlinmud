package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.helper.string.matches
import kotlinmud.io.type.Syntax
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.repository.findMobsForRoom
import kotlinmud.mob.service.MobService
import kotlinmud.room.dao.RoomDAO

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

        val mobs = findMobsForRoom(room)
        return mobs.find { word.matches(it.name) }?.let {
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
