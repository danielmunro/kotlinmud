package kotlinmud.action.contextBuilder

import kotlinmud.action.Context
import kotlinmud.action.type.Status
import kotlinmud.io.Syntax

class CommandContextBuilder : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return Context(syntax, Status.OK, word)
    }
}
