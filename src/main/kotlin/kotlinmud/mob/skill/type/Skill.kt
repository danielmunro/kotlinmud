package kotlinmud.mob.skill.type

import kotlinmud.mob.specialization.type.SpecializationType
import kotlinmud.mob.type.Intent

interface Skill {
    val type: SkillType
    val levelObtained: Map<SpecializationType, Int>
    val difficulty: Map<SpecializationType, LearningDifficulty>
    val intent: Intent
    val invokesOn: SkillInvokesOn
}
