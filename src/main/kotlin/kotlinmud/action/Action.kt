package kotlinmud.action

import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.mob.Disposition
import kotlinmud.mob.HasCosts
import kotlinmud.mob.Invokable
import kotlinmud.mob.RequiresDisposition
import kotlinmud.mob.skill.Cost

class Action(
    val command: Command,
    override val dispositions: List<Disposition>,
    override val syntax: List<Syntax>,
    val mutator: (ActionContextService, Request) -> Response,
    override val costs: List<Cost> = listOf(),
    val chainTo: Command = Command.NOOP
) : RequiresDisposition, Invokable, HasCosts {
    fun isChained(): Boolean {
        return chainTo != Command.NOOP
    }

    override fun invoke(actionContextService: ActionContextService, request: Request): Response {
        return this.mutator(actionContextService, request)
    }
}
