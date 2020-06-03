package kotlinmud.action.model

import kotlinmud.action.ActionContextService
import kotlinmud.action.Invokable
import kotlinmud.action.mustBeAlert
import kotlinmud.action.type.Command
import kotlinmud.io.model.Response
import kotlinmud.io.type.Syntax
import kotlinmud.mob.HasCosts
import kotlinmud.mob.RequiresDisposition
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.type.Disposition

class Action(
    override val command: Command,
    override val dispositions: List<Disposition> = mustBeAlert(),
    override val syntax: List<Syntax> = kotlinmud.io.factory.command(),
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
