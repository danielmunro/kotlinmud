package kotlinmud.mob.skill.type

import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.type.Disposition
import kotlinmud.mob.type.HasCosts
import kotlinmud.mob.type.Intent
import kotlinmud.mob.type.RequiresDisposition
import kotlinmud.mob.type.SpecializationType

interface Skill : RequiresDisposition, HasCosts {
    val type: SkillType
    val levelObtained: Map<SpecializationType, Int>
    val difficulty: Map<SpecializationType, LearningDifficulty>
    override val dispositions: List<Disposition>
    override val costs: List<Cost>
    val intent: Intent
    val invokesOn: SkillInvokesOn
}
