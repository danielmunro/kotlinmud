package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.helper.string.matches
import kotlinmud.io.type.Syntax
import kotlinmud.mob.dao.MobDAO

class MobInRoomContextBuilder(private val mobs: List<MobDAO>) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return mobs.find { matches(it.name, word) }?.let {
            Context<Any>(syntax, Status.OK, it)
        } ?: Context<Any>(
            syntax,
            Status.FAILED,
            "you don't see them anywhere."
        )
    }
}
