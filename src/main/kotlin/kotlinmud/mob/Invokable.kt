package kotlinmud.mob

import kotlinmud.action.ActionContextService
import kotlinmud.action.type.Command
import kotlinmud.io.Response
import kotlinmud.io.Syntax

interface Invokable {
    val syntax: List<Syntax>
    val command: Command

    fun invoke(actionContextService: ActionContextService): Response
}
