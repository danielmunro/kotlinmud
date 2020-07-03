package kotlinmud.mob.skill.impl

import kotlinmud.action.helper.mustBeFighting
import kotlinmud.mob.skill.type.Skill
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.LearningDifficulty
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Disposition
import kotlinmud.mob.type.Intent
import kotlinmud.mob.type.SpecializationType

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
