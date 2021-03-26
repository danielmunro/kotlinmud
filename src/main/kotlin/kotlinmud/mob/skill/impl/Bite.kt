package kotlinmud.mob.skill.impl

import kotlinmud.mob.skill.type.LearningDifficulty
import kotlinmud.mob.skill.type.Skill
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.specialization.type.SpecializationType
import kotlinmud.mob.type.Intent

class Bite : Skill {
    override val type = SkillType.BITE
    override val levelObtained: Map<SpecializationType, Int> = mapOf()
    override val difficulty: Map<SpecializationType, LearningDifficulty> = mapOf()
    override val intent = Intent.OFFENSIVE
    override val invokesOn = SkillInvokesOn.INPUT
}
