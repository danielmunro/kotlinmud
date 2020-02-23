package kotlinmud.mob

import kotlinmud.action.ActionContextService
import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.io.Syntax

interface Invokable {
    val syntax: List<Syntax>

    fun invoke(actionContextService: ActionContextService, request: Request): Response
}