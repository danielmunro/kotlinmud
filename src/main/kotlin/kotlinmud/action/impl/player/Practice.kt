package kotlinmud.action.impl.player

import kotlinmud.action.helper.mustBeStanding
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.attributes.type.Attribute
import kotlinmud.io.factory.createPracticeMessage
import kotlinmud.io.factory.skillToPractice
import kotlinmud.io.type.Syntax
import kotlinmud.mob.model.Mob
import kotlinmud.mob.skill.helper.getLearningDifficultyPracticeAmount
import kotlinmud.mob.skill.type.LearningDifficulty
import kotlinmud.mob.skill.type.Skill
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.specialization.type.SpecializationType
import kotlin.math.roundToInt

fun createPracticeAction(): Action {
    return Action(Command.PRACTICE, mustBeStanding(), skillToPractice()) {
        val skillToPractice = it.get<SkillType>(Syntax.SKILL_TO_PRACTICE)
        val mob = it.getMob()
        mob.skills[skillToPractice] = mob.skills[skillToPractice]!! + it.calculatePracticeGain(mob, skillToPractice)
        mob.practices -= 1
        it.createOkResponse(createPracticeMessage(it.getMob(), skillToPractice))
    }
}

