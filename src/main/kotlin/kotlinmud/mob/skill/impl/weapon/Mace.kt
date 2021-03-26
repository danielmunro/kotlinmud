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

class Mace : Skill, Customization {
    override val type = SkillType.MACE
    override val levelObtained = mapOf(
        warriorAt(1),
        thiefAt(25),
        clericAt(1),
        mageAt(45)
    )
    override val difficulty = mapOf(
        Pair(SpecializationType.WARRIOR, LearningDifficulty.EASY),
        Pair(SpecializationType.THIEF, LearningDifficulty.NORMAL),
        Pair(SpecializationType.CLERIC, LearningDifficulty.EASY),
        Pair(SpecializationType.MAGE, LearningDifficulty.VERY_HARD)
    )
    override val intent = Intent.NEUTRAL
    override val invokesOn = SkillInvokesOn.ATTACK_ROUND
    override val name = "mace"
    override val points = 4
    override val creationGroupType = CreationGroupType.SKILL
    override val helpText = "tbd"
}
