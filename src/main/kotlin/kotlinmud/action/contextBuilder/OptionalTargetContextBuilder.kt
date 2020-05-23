package kotlinmud.action.contextBuilder

import kotlinmud.Noun
import kotlinmud.action.Context
import kotlinmud.action.type.Status
import kotlinmud.io.Syntax
import kotlinmud.mob.Mob
import kotlinmud.string.matches

class OptionalTargetContextBuilder(
    private val actionCreator: Mob,
    private val possibleTargets: List<Noun>
) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        if (word == "") {
            return Context(syntax, Status.OK, actionCreator)
        }
        return possibleTargets.find {
            matches(it.name, word)
        }?.let {
            Context<Any>(syntax, Status.OK, it)
        } ?: Context<Any>(syntax, Status.ERROR, "you don't see it anywhere.")
    }
}
