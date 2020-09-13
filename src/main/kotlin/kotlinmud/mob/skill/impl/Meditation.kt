package kotlinmud.mob.skill.impl

import kotlinmud.action.helper.mustBeFighting
import kotlinmud.mob.skill.factory.clericAt
import kotlinmud.mob.skill.factory.mageAt
import kotlinmud.mob.skill.factory.normalForCleric
import kotlinmud.mob.skill.factory.normalForMage
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.Skill
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Intent

class Meditation : Skill, Customization {
    override val type = SkillType.MEDITATION
    override val creationGroupType = CreationGroupType.SKILL
    override val name = "meditation"
    override val points = 8
    override val levelObtained = mapOf(
        clericAt(5),
        mageAt(5)
    )
    override val difficulty = mapOf(
        normalForCleric(),
        normalForMage()
    )
    override val dispositions = mustBeFighting()
    override val costs = listOf<Cost>()
    override val intent = Intent.NEUTRAL
    override val invokesOn = SkillInvokesOn.TICK
}
