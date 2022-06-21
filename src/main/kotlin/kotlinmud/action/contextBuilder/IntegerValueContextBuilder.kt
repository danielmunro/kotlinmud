package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.io.type.Syntax

class IntegerValueContextBuilder : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return try {
            Context(Syntax.VALUE, Status.OK, word.toInt())
        } catch (e: NumberFormatException) {
            Context(Syntax.VALUE, Status.ERROR, "not a number")
        }
    }
}
