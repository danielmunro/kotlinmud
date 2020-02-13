package kotlinmud.action

import kotlinmud.io.Syntax

class ActionContextList(val contexts: MutableList<Context<Any>>) {
    inline fun <reified T> getResultBySyntax(syntax: Syntax): T {
        return contexts
            .find { it.syntax == syntax }
            .let { if (it != null && it.result is T) it.result else throw Exception() }
    }

    fun getError(): Context<Any>? {
        return contexts.find { it.status == Status.FAILED }
    }
}
