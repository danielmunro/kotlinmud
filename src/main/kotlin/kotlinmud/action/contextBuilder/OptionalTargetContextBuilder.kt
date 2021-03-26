package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Action
import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.helper.Noun
import kotlinmud.helper.string.matches
import kotlinmud.io.type.Syntax
import kotlinmud.mob.model.Fight
import kotlinmud.mob.model.Mob
import kotlinmud.mob.type.Intent

class OptionalTargetContextBuilder(
    private val actionCreator: Mob,
    private val possibleTargets: List<Any>,
    private val action: Action,
    private val fight: Fight? = null,
) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        if (word == "") {
            return when (action.intent) {
                Intent.OFFENSIVE -> fight?.getOpponentFor(actionCreator)?.let {
                    Context<Any>(syntax, Status.OK, it)
                } ?: Context<Any>(syntax, Status.ERROR, "who are you targeting?")
                Intent.NEUTRAL -> Context<Any>(syntax, Status.OK, actionCreator)
                Intent.PROTECTIVE -> Context<Any>(syntax, Status.OK, actionCreator)
            }
        }
        return possibleTargets.find {
            return@find if (it is Noun) {
                word.matches(it.name)
            } else false
        }?.let {
            Context<Any>(syntax, Status.OK, it)
        } ?: Context<Any>(syntax, Status.ERROR, "you don't see it anywhere.")
    }
}
