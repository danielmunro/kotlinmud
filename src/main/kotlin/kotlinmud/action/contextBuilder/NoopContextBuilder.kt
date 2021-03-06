package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.io.type.Syntax

class NoopContextBuilder : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return Context<Any>(syntax, Status.ERROR, "what was that?")
    }
}
