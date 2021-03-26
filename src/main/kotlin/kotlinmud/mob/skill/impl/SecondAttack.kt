package kotlinmud.mob.skill.impl

import kotlinmud.mob.skill.factory.hardForThief
import kotlinmud.mob.skill.factory.normalForWarrior
import kotlinmud.mob.skill.factory.thiefAt
import kotlinmud.mob.skill.factory.warriorAt
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.Skill
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Intent

class SecondAttack : Skill, Customization {
    override val type = SkillType.SECOND_ATTACK
    override val creationGroupType = CreationGroupType.SKILL
    override val name = "second attack"
    override val points = 8
    override val levelObtained = mapOf(
        thiefAt(30),
        warriorAt(5)
    )
    override val difficulty = mapOf(
        normalForWarrior(),
        hardForThief()
    )
    override val intent = Intent.OFFENSIVE
    override val invokesOn = SkillInvokesOn.ATTACK_ROUND
    override val helpText = "tbd"
}
