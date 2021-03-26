package kotlinmud.mob.skill.impl.weapon

import kotlinmud.mob.skill.factory.clericAt
import kotlinmud.mob.skill.factory.hardForThief
import kotlinmud.mob.skill.factory.hardForWarrior
import kotlinmud.mob.skill.factory.mageAt
import kotlinmud.mob.skill.factory.normalForCleric
import kotlinmud.mob.skill.factory.normalForMage
import kotlinmud.mob.skill.factory.thiefAt
import kotlinmud.mob.skill.factory.warriorAt
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.Skill
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Intent

class Staff : Skill, Customization {
    override val type = SkillType.STAFF
    override val levelObtained = mapOf(
        warriorAt(45),
        thiefAt(45),
        clericAt(30),
        mageAt(15)
    )
    override val difficulty = mapOf(
        hardForWarrior(),
        hardForThief(),
        normalForMage(),
        normalForCleric()
    )
    override val intent = Intent.NEUTRAL
    override val invokesOn = SkillInvokesOn.ATTACK_ROUND
    override val name = "sword"
    override val points = 4
    override val creationGroupType = CreationGroupType.SKILL
    override val helpText = "tbd"
}
