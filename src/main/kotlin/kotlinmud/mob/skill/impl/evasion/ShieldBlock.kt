package kotlinmud.mob.skill.impl.evasion

import kotlinmud.action.helper.mustBeFighting
import kotlinmud.mob.skill.factory.normalForThief
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

class ShieldBlock : Skill, Customization {
    override val type = SkillType.SHIELD_BLOCK
    override val creationGroupType = CreationGroupType.SKILL
    override val name = "shield block"
    override val points = 4
    override val levelObtained = mapOf(
        warriorAt(1),
        thiefAt(15)
    )
    override val difficulty = mapOf(
        normalForThief(),
        normalForWarrior()
    )
    override val dispositions = mustBeFighting()
    override val costs = listOf<Cost>()
    override val intent = Intent.PROTECTIVE
    override val invokesOn = SkillInvokesOn.ATTACK_ROUND
}
