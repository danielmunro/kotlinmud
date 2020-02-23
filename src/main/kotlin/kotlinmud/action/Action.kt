package kotlinmud.action

import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.mob.Disposition
import kotlinmud.mob.Invokable
import kotlinmud.mob.RequiresDisposition

class Action(
    val command: Command,
    override val dispositions: List<Disposition>,
    override val syntax: List<Syntax>,
    val mutator: (ActionContextService, Request) -> Response,
    val chainTo: Command = Command.NOOP
) : RequiresDisposition, Invokable {
    fun isChained(): Boolean {
        return chainTo != Command.NOOP
    }

    override fun invoke(actionContextService: ActionContextService, request: Request): Response {
        return this.mutator(actionContextService, request)
    }
}
