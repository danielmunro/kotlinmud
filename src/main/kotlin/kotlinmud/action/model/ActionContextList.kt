package kotlinmud.action.model

import kotlinmud.action.type.Status
import kotlinmud.io.type.Syntax

data class ActionContextList(private val contexts: MutableList<Context<Any>>) {
    fun <T> getResultBySyntax(syntax: Syntax): T {
        @Suppress("UNCHECKED_CAST")
        return contexts
            .find { it.syntax == syntax }
            .let { if (it != null) it.result as T else throw Exception() }
    }

    fun getBadResult(): Context<Any>? {
        return contexts.find { it.status != Status.OK }
    }
}
