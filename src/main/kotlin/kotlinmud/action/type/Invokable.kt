package kotlinmud.action.type

import kotlinmud.action.service.ActionContextService
import kotlinmud.io.model.Response
import kotlinmud.io.type.Syntax

interface Invokable {
    val syntax: List<Syntax>
    val command: Command

    fun invoke(actionContextService: ActionContextService): Response
}
