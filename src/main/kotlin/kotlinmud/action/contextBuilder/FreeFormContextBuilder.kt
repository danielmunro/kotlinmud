package kotlinmud.action.contextBuilder

import kotlinmud.action.Context
import kotlinmud.action.type.Status
import kotlinmud.io.Syntax

class FreeFormContextBuilder(private val input: List<String>) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return Context(Syntax.FREE_FORM, Status.OK, input.drop(input.indexOf(word)).joinToString(" "))
    }
}
