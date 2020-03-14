package kotlinmud.mob.skill

import kotlinmud.mob.skill.impl.*

fun createSkillList(): List<Skill> {
    return listOf(
        Bash(),
        Berserk(),
        Bite(),
        ShieldBlock(),
        Parry(),
        Dodge()
    )
}
