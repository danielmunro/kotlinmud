package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeStanding
import kotlinmud.io.Message
import kotlinmud.io.Syntax
import kotlinmud.mob.skill.SkillType

fun createPracticeAction(): Action {
    return Action(
        Command.PRACTICE,
        mustBeStanding(),
        listOf(Syntax.COMMAND, Syntax.SKILL_TO_PRACTICE),
        {
            val skillType: SkillType = it.get(Syntax.SKILL_TO_PRACTICE)
            it.getMob().practices -= 1
            it.getMob().skills[skillType] = it.getMob().skills[skillType]!!.plus(1)
            it.createResponse(
                Message(
                    "you practice $skillType.",
                    "${it.getMob()} practices $skillType."
                )
            )
        })
}
