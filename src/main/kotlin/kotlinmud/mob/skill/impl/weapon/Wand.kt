package kotlinmud.mob.skill.impl.weapon

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.mob.skill.factory.clericAt
import kotlinmud.mob.skill.factory.mageAt
import kotlinmud.mob.skill.factory.thiefAt
import kotlinmud.mob.skill.factory.warriorAt
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.LearningDifficulty
import kotlinmud.mob.skill.type.Skill
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.specialization.type.SpecializationType
import kotlinmud.mob.type.Intent

class Wand : Skill, Customization {
    override val type = SkillType.WAND
    override val levelObtained = mapOf(
        warriorAt(30),
        thiefAt(30),
        clericAt(5),
        mageAt(1)
    )
    override val difficulty = mapOf(
        Pair(SpecializationType.WARRIOR, LearningDifficulty.VERY_HARD),
        Pair(SpecializationType.THIEF, LearningDifficulty.VERY_HARD),
        Pair(SpecializationType.CLERIC, LearningDifficulty.NORMAL),
        Pair(SpecializationType.MAGE, LearningDifficulty.EASY)
    )
    override val dispositions = mustBeAlert()
    override val costs = listOf<Cost>()
    override val intent = Intent.NEUTRAL
    override val invokesOn = SkillInvokesOn.ATTACK_ROUND
    override val name = "wand"
    override val points = 4
    override val creationGroupType = CreationGroupType.SKILL
    override val helpText = "tbd"
}
