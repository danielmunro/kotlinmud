package kotlinmud.action

import kotlinmud.io.Syntax

class ContextCollection(private val contexts: MutableList<Context<Any>>) {
    fun <T> getResultBySyntax(syntax: Syntax): T? {
        val context = contexts.find{ it.syntax == syntax }
        if (context != null) {
            @Suppress("UNCHECKED_CAST")
            return context.result as T
        }
        return null
    }

    fun getError(): Context<Any>? {
        return contexts.find { it.status == Status.FAILED }
    }
}