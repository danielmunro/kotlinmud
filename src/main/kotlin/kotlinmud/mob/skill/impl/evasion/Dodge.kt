package kotlinmud.mob.skill.impl.evasion

import kotlinmud.action.helper.mustBeFighting
import kotlinmud.mob.skill.factory.clericAt
import kotlinmud.mob.skill.factory.easyForThief
import kotlinmud.mob.skill.factory.easyForWarrior
import kotlinmud.mob.skill.factory.hardForMage
import kotlinmud.mob.skill.factory.mageAt
import kotlinmud.mob.skill.factory.normalForCleric
import kotlinmud.mob.skill.factory.thiefAt
import kotlinmud.mob.skill.factory.warriorAt
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.Skill
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Intent

class Dodge : Skill, Customization {
    override val type = SkillType.DODGE
    override val creationGroupType = CreationGroupType.SKILL
    override val name = "dodge"
    override val points = 8
    override val levelObtained = mapOf(
        thiefAt(5),
        warriorAt(5),
        clericAt(20),
        mageAt(35)
    )
    override val difficulty = mapOf(
        easyForWarrior(),
        easyForThief(),
        normalForCleric(),
        hardForMage()
    )
    override val dispositions = mustBeFighting()
    override val costs = listOf<Cost>()
    override val intent = Intent.PROTECTIVE
    override val invokesOn = SkillInvokesOn.ATTACK_ROUND
    override val helpText = "tbd"
}
