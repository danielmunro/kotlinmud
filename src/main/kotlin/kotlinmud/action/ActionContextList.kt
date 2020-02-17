package kotlinmud.action

import kotlinmud.io.Syntax

class ActionContextList(val contexts: MutableList<Context<Any>>) {
    fun <T> getResultBySyntax(syntax: Syntax): T {
        @Suppress("UNCHECKED_CAST")
        return contexts
            .find { it.syntax == syntax }
            .let { if (it != null) it.result as T else throw Exception() }
    }

    fun getError(): Context<Any>? {
        return contexts.find { it.status == Status.FAILED }
    }
}
