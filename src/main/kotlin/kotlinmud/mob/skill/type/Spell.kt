package kotlinmud.mob.skill.type

import kotlinmud.io.model.Message
import kotlinmud.mob.model.Mob

interface Spell : Skill {
    override val invokesOn
        get() = SkillInvokesOn.INPUT

    fun cast(caster: Mob, target: Mob)

    fun createMessage(caster: Mob, target: Mob): Message
}
