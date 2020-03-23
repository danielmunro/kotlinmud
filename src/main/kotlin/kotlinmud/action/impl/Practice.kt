package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeStanding
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.mob.skill.SkillType

fun createPracticeAction(): Action {
    return Action(
        Command.PRACTICE,
        mustBeStanding(),
        listOf(Syntax.COMMAND, Syntax.SKILL_TO_PRACTICE),
        { svc: ActionContextService, request: Request ->
            val skillType: SkillType = svc.get(Syntax.SKILL_TO_PRACTICE)
            request.mob.practices -= 1
            request.mob.skills[skillType] = request.mob.skills[skillType]!!.plus(1)
            svc.createResponse(
                Message(
                    "you practice $skillType.",
                    "${request.mob.name} practices $skillType."
                )
            )
        })
}
