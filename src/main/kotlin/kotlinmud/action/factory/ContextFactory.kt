package kotlinmud.action.factory

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.io.type.Syntax

fun failedContext(syntax: Syntax, message: String): Context<Any> {
    return Context(syntax, Status.FAILED, message)
}
