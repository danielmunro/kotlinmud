package kotlinmud.mob.skill.impl.evasion

import kotlinmud.mob.skill.factory.clericAt
import kotlinmud.mob.skill.factory.hardForCleric
import kotlinmud.mob.skill.factory.mageAt
import kotlinmud.mob.skill.factory.normalForThief
import kotlinmud.mob.skill.factory.normalForWarrior
import kotlinmud.mob.skill.factory.thiefAt
import kotlinmud.mob.skill.factory.veryHardForMage
import kotlinmud.mob.skill.factory.warriorAt
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.Skill
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Intent

class Parry : Skill, Customization {
    override val type = SkillType.PARRY
    override val creationGroupType = CreationGroupType.SKILL
    override val name = "parry"
    override val points = 8
    override val levelObtained = mapOf(
        thiefAt(1),
        warriorAt(15),
        clericAt(30),
        mageAt(45)
    )
    override val difficulty = mapOf(
        normalForWarrior(),
        normalForThief(),
        hardForCleric(),
        veryHardForMage()
    )
    override val intent = Intent.PROTECTIVE
    override val invokesOn = SkillInvokesOn.ATTACK_ROUND
    override val helpText = "tbd"
}
