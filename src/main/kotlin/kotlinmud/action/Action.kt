package kotlinmud.action

import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.mob.Disposition
import kotlinmud.mob.HasCosts
import kotlinmud.mob.Invokable
import kotlinmud.mob.RequiresDisposition
import kotlinmud.mob.skill.Cost

class Action(
    override val command: Command,
    override val dispositions: List<Disposition>,
    override val syntax: List<Syntax>,
    override val costs: List<Cost> = listOf(),
    val chainTo: Command = Command.NOOP,
    val mutator: (ActionContextService) -> Response
) : RequiresDisposition, Invokable, HasCosts {
    fun isChained(): Boolean {
        return chainTo != Command.NOOP
    }

    override fun invoke(actionContextService: ActionContextService): Response {
        return this.mutator(actionContextService)
    }
}
