package kotlinmud.mob.skill

import kotlinmud.mob.*

interface Skill : RequiresDisposition, Invokable {
    val type: SkillType
    val levelObtained: Map<SpecializationType, Int>
    val difficulty: Map<SpecializationType, LearningDifficulty>
    override val dispositions: List<Disposition>
    val costs: List<Cost>
    val intent: Intent
}
