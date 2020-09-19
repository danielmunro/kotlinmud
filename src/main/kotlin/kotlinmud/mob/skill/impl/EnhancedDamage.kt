package kotlinmud.mob.skill.impl

import kotlinmud.action.helper.mustBeFighting
import kotlinmud.mob.skill.factory.hardForThief
import kotlinmud.mob.skill.factory.normalForWarrior
import kotlinmud.mob.skill.factory.thiefAt
import kotlinmud.mob.skill.factory.warriorAt
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.Skill
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Intent

class EnhancedDamage : Skill, Customization {
    override val type = SkillType.ENHANCED_DAMAGE
    override val creationGroupType = CreationGroupType.SKILL
    override val name = "enhanced damage"
    override val points = 8
    override val levelObtained = mapOf(
        thiefAt(45),
        warriorAt(5)
    )
    override val difficulty = mapOf(
        normalForWarrior(),
        hardForThief()
    )
    override val dispositions = mustBeFighting()
    override val costs = listOf<Cost>()
    override val intent = Intent.NEUTRAL
    override val invokesOn = SkillInvokesOn.ATTACK_ROUND
    override val helpText = "tbd"
}
