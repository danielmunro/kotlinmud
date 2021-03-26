package kotlinmud.mob.skill.impl.weapon

import kotlinmud.mob.skill.factory.clericAt
import kotlinmud.mob.skill.factory.mageAt
import kotlinmud.mob.skill.factory.thiefAt
import kotlinmud.mob.skill.factory.warriorAt
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.LearningDifficulty
import kotlinmud.mob.skill.type.Skill
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.specialization.type.SpecializationType
import kotlinmud.mob.type.Intent

class Unarmed : Skill, Customization {
    override val type = SkillType.UNARMED
    override val levelObtained = mapOf(
        warriorAt(1),
        thiefAt(10),
        clericAt(20),
        mageAt(20)
    )
    override val difficulty = mapOf(
        Pair(SpecializationType.WARRIOR, LearningDifficulty.NORMAL),
        Pair(SpecializationType.THIEF, LearningDifficulty.HARD),
        Pair(SpecializationType.CLERIC, LearningDifficulty.VERY_HARD),
        Pair(SpecializationType.MAGE, LearningDifficulty.VERY_HARD)
    )
    override val intent = Intent.NEUTRAL
    override val invokesOn = SkillInvokesOn.ATTACK_ROUND
    override val name = "unarmed"
    override val points = 4
    override val creationGroupType = CreationGroupType.SKILL
    override val helpText = "tbd"
}
