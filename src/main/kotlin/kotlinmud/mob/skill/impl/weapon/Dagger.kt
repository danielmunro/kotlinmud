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

class Dagger : Skill, Customization {
    override val type = SkillType.DAGGER
    override val levelObtained = mapOf(
        warriorAt(1),
        thiefAt(1),
        clericAt(15),
        mageAt(15)
    )
    override val difficulty = mapOf(
        Pair(SpecializationType.WARRIOR, LearningDifficulty.EASY),
        Pair(SpecializationType.THIEF, LearningDifficulty.EASY),
        Pair(SpecializationType.CLERIC, LearningDifficulty.NORMAL),
        Pair(SpecializationType.MAGE, LearningDifficulty.NORMAL)
    )
    override val intent = Intent.NEUTRAL
    override val invokesOn = SkillInvokesOn.ATTACK_ROUND
    override val name = "dagger"
    override val points = 4
    override val creationGroupType = CreationGroupType.SKILL
    override val helpText = "tbd"
}
