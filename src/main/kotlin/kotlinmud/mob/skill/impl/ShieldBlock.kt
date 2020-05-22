package kotlinmud.mob.skill.impl

import kotlinmud.action.mustBeFighting
import kotlinmud.mob.skill.Cost
import kotlinmud.mob.skill.LearningDifficulty
import kotlinmud.mob.skill.Skill
import kotlinmud.mob.skill.SkillInvokesOn
import kotlinmud.mob.skill.SkillType
import kotlinmud.mob.type.Disposition
import kotlinmud.mob.type.Intent
import kotlinmud.mob.type.SpecializationType

class ShieldBlock : Skill {
    override val type: SkillType = SkillType.SHIELD_BLOCK
    override val levelObtained: Map<SpecializationType, Int> = mapOf(
        Pair(SpecializationType.WARRIOR, 1),
        Pair(SpecializationType.THIEF, 15)
    )
    override val difficulty: Map<SpecializationType, LearningDifficulty> = mapOf(
        Pair(SpecializationType.WARRIOR, LearningDifficulty.NORMAL),
        Pair(SpecializationType.THIEF, LearningDifficulty.NORMAL)
    )
    override val dispositions: List<Disposition> = mustBeFighting()
    override val costs: List<Cost> = listOf()
    override val intent: Intent = Intent.PROTECTIVE
    override val invokesOn: SkillInvokesOn = SkillInvokesOn.ATTACK_ROUND
}
