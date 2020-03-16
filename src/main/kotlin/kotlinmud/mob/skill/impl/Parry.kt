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

class Parry : Skill {
    override val type: SkillType = SkillType.PARRY
    override val levelObtained: Map<SpecializationType, Int> = mapOf(
        Pair(SpecializationType.THIEF, 1),
        Pair(SpecializationType.WARRIOR, 15),
        Pair(SpecializationType.CLERIC, 30),
        Pair(SpecializationType.MAGE, 45)
    )
    override val difficulty: Map<SpecializationType, LearningDifficulty> = mapOf(
        Pair(SpecializationType.WARRIOR, LearningDifficulty.NORMAL),
        Pair(SpecializationType.THIEF, LearningDifficulty.NORMAL),
        Pair(SpecializationType.CLERIC, LearningDifficulty.HARD),
        Pair(SpecializationType.MAGE, LearningDifficulty.VERY_HARD)
    )
    override val dispositions: List<Disposition> = mustBeFighting()
    override val costs: List<Cost> = listOf()
    override val intent: Intent = Intent.PROTECTIVE
    override val invokesOn: SkillInvokesOn = SkillInvokesOn.ATTACK_ROUND
}
