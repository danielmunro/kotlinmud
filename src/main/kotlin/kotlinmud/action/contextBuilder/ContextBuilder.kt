package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.io.type.Syntax

interface ContextBuilder {
    fun build(syntax: Syntax, word: String): Context<Any>
}
