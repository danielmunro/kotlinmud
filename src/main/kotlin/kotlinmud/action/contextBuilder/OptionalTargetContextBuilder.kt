package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.helper.Noun
import kotlinmud.helper.string.matches
import kotlinmud.io.type.Syntax
import kotlinmud.mob.model.Mob

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
