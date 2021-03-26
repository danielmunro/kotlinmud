package kotlinmud.mob.skill.impl

import kotlinmud.mob.skill.factory.normalForThief
import kotlinmud.mob.skill.factory.thiefAt
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.Skill
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Intent

class Hamstring : Skill, Customization {
    override val type = SkillType.HAMSTRING
    override val levelObtained = mapOf(
        thiefAt(15)
    )
    override val difficulty = mapOf(
        normalForThief()
    )
    override val intent = Intent.OFFENSIVE
    override val invokesOn = SkillInvokesOn.INPUT
    override val creationGroupType = CreationGroupType.SKILL
    override val name = "hamstring"
    override val points = 6
    override val helpText = "tbd"
}
