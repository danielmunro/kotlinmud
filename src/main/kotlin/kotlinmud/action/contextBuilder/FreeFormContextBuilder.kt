package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.io.type.Syntax

class FreeFormContextBuilder(private val input: List<String>) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        val index = input.indexOf(word)
        if (index == -1) {
            return Context(
                Syntax.FREE_FORM,
                Status.OK,
                ""
            )
        }
        return Context(
            Syntax.FREE_FORM,
            Status.OK,
            input.drop(index).joinToString(" ")
        )
    }
}
