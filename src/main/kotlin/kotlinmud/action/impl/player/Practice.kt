package kotlinmud.action.impl.player

import kotlinmud.action.helper.mustBeStanding
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createPracticeMessage
import kotlinmud.io.factory.skillToPractice
import kotlinmud.io.type.Syntax
import kotlinmud.mob.skill.type.SkillType

fun createPracticeAction(): Action {
    return Action(Command.PRACTICE, mustBeStanding(), skillToPractice()) {
        with(it.get<SkillType>(Syntax.SKILL_TO_PRACTICE)) {
            it.practice(this)
            it.createOkResponse(createPracticeMessage(it.getMob(), this))
        }
    }
}
