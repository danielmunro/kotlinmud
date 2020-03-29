package kotlinmud.mob.skill

import kotlinmud.mob.Disposition
import kotlinmud.mob.HasCosts
import kotlinmud.mob.Intent
import kotlinmud.mob.RequiresDisposition
import kotlinmud.mob.SpecializationType

interface Skill : RequiresDisposition, HasCosts {
    val type: SkillType
    val levelObtained: Map<SpecializationType, Int>
    val difficulty: Map<SpecializationType, LearningDifficulty>
    override val dispositions: List<Disposition>
    override val costs: List<Cost>
    val intent: Intent
    val invokesOn: SkillInvokesOn
}
