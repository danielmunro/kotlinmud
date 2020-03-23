package kotlinmud.action.contextBuilder

import kotlinmud.action.Context
import kotlinmud.io.Syntax
import kotlinmud.mob.Mob

class TrainableContextBuilder(private val mob: Mob) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        TODO("Not yet implemented")
    }
}
