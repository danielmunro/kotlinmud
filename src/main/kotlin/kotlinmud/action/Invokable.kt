package kotlinmud.action

import kotlinmud.action.type.Command
import kotlinmud.io.model.Response
import kotlinmud.io.type.Syntax

interface Invokable {
    val syntax: List<Syntax>
    val command: Command

    fun invoke(actionContextService: ActionContextService): Response
}
