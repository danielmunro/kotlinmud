package kotlinmud.mob.skill.impl

import kotlinmud.action.mustBeFighting
import kotlinmud.mob.Disposition
import kotlinmud.mob.Intent
import kotlinmud.mob.SpecializationType
import kotlinmud.mob.skill.Cost
import kotlinmud.mob.skill.LearningDifficulty
import kotlinmud.mob.skill.Skill
import kotlinmud.mob.skill.SkillInvokesOn
import kotlinmud.mob.skill.SkillType

class Dodge : Skill {
    override val type: SkillType = SkillType.DODGE
    override val levelObtained: Map<SpecializationType, Int> = mapOf(
        Pair(SpecializationType.THIEF, 5),
        Pair(SpecializationType.WARRIOR, 5),
        Pair(SpecializationType.CLERIC, 20),
        Pair(SpecializationType.MAGE, 35)
    )
    override val difficulty: Map<SpecializationType, LearningDifficulty> = mapOf(
        Pair(SpecializationType.WARRIOR, LearningDifficulty.EASY),
        Pair(SpecializationType.THIEF, LearningDifficulty.EASY),
        Pair(SpecializationType.CLERIC, LearningDifficulty.NORMAL),
        Pair(SpecializationType.MAGE, LearningDifficulty.HARD)
    )
    override val dispositions: List<Disposition> = mustBeFighting()
    override val costs: List<Cost> = listOf()
    override val intent: Intent = Intent.PROTECTIVE
    override val invokesOn: SkillInvokesOn = SkillInvokesOn.ATTACK_ROUND
}
