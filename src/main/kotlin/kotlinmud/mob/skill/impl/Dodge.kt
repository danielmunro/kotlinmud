package kotlinmud.mob.skill.impl

import kotlinmud.action.helper.mustBeFighting
import kotlinmud.mob.skill.Skill
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.LearningDifficulty
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Disposition
import kotlinmud.mob.type.Intent
import kotlinmud.mob.type.SpecializationType

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
