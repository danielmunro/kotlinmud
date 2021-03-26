package kotlinmud.mob.skill.impl

import kotlinmud.mob.skill.factory.normalForThief
import kotlinmud.mob.skill.factory.normalForWarrior
import kotlinmud.mob.skill.factory.thiefAt
import kotlinmud.mob.skill.factory.warriorAt
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.Skill
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Intent

class FastHealing : Skill, Customization {
    override val type = SkillType.FAST_HEALING
    override val creationGroupType = CreationGroupType.SKILL
    override val name = "fast healing"
    override val points = 8
    override val levelObtained = mapOf(
        warriorAt(20),
        thiefAt(20)
    )
    override val difficulty = mapOf(
        normalForThief(),
        normalForWarrior()
    )
    override val intent = Intent.NEUTRAL
    override val invokesOn = SkillInvokesOn.TICK
    override val helpText = "tbd"
}
