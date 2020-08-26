package kotlinmud.mob.skill.helper

import kotlinmud.mob.skill.impl.Bash
import kotlinmud.mob.skill.impl.Berserk
import kotlinmud.mob.skill.impl.Bite
import kotlinmud.mob.skill.impl.Dodge
import kotlinmud.mob.skill.impl.Parry
import kotlinmud.mob.skill.impl.ShieldBlock
import kotlinmud.mob.skill.impl.healing.CureLight
import kotlinmud.mob.skill.impl.healing.CureSerious
import kotlinmud.mob.skill.impl.healing.Heal
import kotlinmud.mob.skill.impl.illusion.Invisibility
import kotlinmud.mob.skill.impl.malediction.Blind
import kotlinmud.mob.skill.impl.malediction.Curse
import kotlinmud.mob.skill.type.Skill

fun createSkillList(): List<Skill> {
    return listOf(
        Bash(),
        Berserk(),
        Bite(),
        ShieldBlock(),
        Parry(),
        Dodge(),
        Invisibility(),

        // healing
        CureLight(),
        CureSerious(),
        Heal(),

        // malediction
        Blind(),
        Curse()
    )
}
