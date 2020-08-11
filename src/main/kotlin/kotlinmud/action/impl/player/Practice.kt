package kotlinmud.action.impl.player

import kotlinmud.action.helper.mustBeStanding
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createPracticeMessage
import kotlinmud.io.factory.skillToPractice
import kotlinmud.io.type.Syntax
import kotlinmud.mob.skill.dao.SkillDAO

fun createPracticeAction(): Action {
    return Action(Command.PRACTICE, mustBeStanding(), skillToPractice()) {
        with(it.get<SkillDAO>(Syntax.SKILL_TO_PRACTICE)) {
            it.practice(this.type)
            it.createOkResponse(createPracticeMessage(it.getMob(), this.type))
        }
    }
}
