package kotlinmud.mob.skill.impl

import kotlinmud.mob.skill.factory.easyForThief
import kotlinmud.mob.skill.factory.easyForWarrior
import kotlinmud.mob.skill.factory.thiefAt
import kotlinmud.mob.skill.factory.warriorAt
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.Skill
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Intent

class Trip : Skill, Customization {
    override val type = SkillType.TRIP
    override val creationGroupType = CreationGroupType.SKILL
    override val name = "trip"
    override val points = 4
    override val levelObtained = mapOf(
        thiefAt(15),
        warriorAt(5)
    )
    override val difficulty = mapOf(
        easyForWarrior(),
        easyForThief()
    )
    override val intent = Intent.OFFENSIVE
    override val invokesOn = SkillInvokesOn.ATTACK_ROUND
    override val helpText = "tbd"
}
