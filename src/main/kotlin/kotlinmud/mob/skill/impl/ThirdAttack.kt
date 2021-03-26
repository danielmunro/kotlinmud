package kotlinmud.mob.skill.impl

import kotlinmud.mob.skill.factory.hardForWarrior
import kotlinmud.mob.skill.factory.warriorAt
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.Skill
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Intent

class ThirdAttack : Skill, Customization {
    override val type = SkillType.THIRD_ATTACK
    override val creationGroupType = CreationGroupType.SKILL
    override val name = "third attack"
    override val points = 10
    override val levelObtained = mapOf(
        warriorAt(35)
    )
    override val difficulty = mapOf(
        hardForWarrior()
    )
    override val intent = Intent.OFFENSIVE
    override val invokesOn = SkillInvokesOn.ATTACK_ROUND
    override val helpText = "tbd"
}
