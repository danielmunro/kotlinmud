package kotlinmud.mob.skill

import kotlinmud.mob.skill.impl.Bash
import kotlinmud.mob.skill.impl.Berserk
import kotlinmud.mob.skill.impl.Bite
import kotlinmud.mob.skill.impl.ShieldBlock

fun createSkillList(): List<Skill> {
    return listOf(
        Bash(),
        Berserk(),
        Bite(),
        ShieldBlock()
    )
}
