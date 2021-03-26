package kotlinmud.mob.skill.impl

import kotlinmud.mob.skill.factory.easyForWarrior
import kotlinmud.mob.skill.factory.normalForThief
import kotlinmud.mob.skill.factory.thiefAt
import kotlinmud.mob.skill.factory.warriorAt
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.Skill
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Intent

class Disarm : Skill, Customization {
    override val type = SkillType.DISARM
    override val levelObtained = mapOf(
        warriorAt(5),
        thiefAt(30)
    )
    override val difficulty = mapOf(
        easyForWarrior(),
        normalForThief()
    )
    override val intent = Intent.OFFENSIVE
    override val invokesOn = SkillInvokesOn.INPUT
    override val creationGroupType = CreationGroupType.SKILL
    override val name = "disarm"
    override val points = 6
    override val helpText = "tbd"
}
