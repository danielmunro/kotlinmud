package kotlinmud.action.builder

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.model.Action
import kotlinmud.action.service.ActionContextService
import kotlinmud.action.type.Command
import kotlinmud.io.factory.command
import kotlinmud.io.model.Response
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.Skill
import kotlinmud.mob.type.Intent
import kotlinmud.mob.type.Role

class ActionBuilder(var command: Command) {
    var dispositions = mustBeAlert()
    var syntax = command()
    var argumentOrder: List<Int>? = null
    var costs = listOf<Cost>()
    var chainTo = Command.NOOP
    var minimumRole = Role.Player
    var skill: Skill? = null
    var intent = Intent.NEUTRAL

    infix fun build(mutator: (ActionContextService) -> Response): Action {
        return Action(
            command,
            dispositions,
            syntax,
            argumentOrder ?: syntax.mapIndexed { i, _ -> i },
            costs,
            chainTo,
            minimumRole,
            skill,
            intent,
            mutator,
        )
    }
}
