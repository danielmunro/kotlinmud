package kotlinmud.action.contextBuilder

import kotlinmud.action.Context
import kotlinmud.action.Status
import kotlinmud.io.Syntax

class FreeFormContextBuilder(private val input: List<String>) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return Context(Syntax.FREE_FORM, Status.OK, input.drop(0).joinToString(" "))
    }
}