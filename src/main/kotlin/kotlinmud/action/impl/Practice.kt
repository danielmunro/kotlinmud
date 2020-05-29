package kotlinmud.action.impl

import kotlinmud.action.model.Action
import kotlinmud.action.mustBeStanding
import kotlinmud.action.type.Command
import kotlinmud.io.MessageBuilder
import kotlinmud.io.Syntax
import kotlinmud.io.skillToPractice
import kotlinmud.mob.skill.SkillType

fun createPracticeAction(): Action {
    return Action(Command.PRACTICE, mustBeStanding(), skillToPractice()) {
        val skillType: SkillType = it.get(Syntax.SKILL_TO_PRACTICE)
        it.getMob().practices -= 1
        it.getMob().skills[skillType] = it.getMob().skills[skillType]!!.plus(1)
        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("you practice $skillType.")
                .toObservers("${it.getMob()} practices $skillType.")
                .build()
        )
    }
}
