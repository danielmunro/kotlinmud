package kotlinmud.mob.skill.helper

import kotlinmud.mob.skill.impl.Bash
import kotlinmud.mob.skill.impl.Berserk
import kotlinmud.mob.skill.impl.Bite
import kotlinmud.mob.skill.impl.Disarm
import kotlinmud.mob.skill.impl.EnhancedDamage
import kotlinmud.mob.skill.impl.FastHealing
import kotlinmud.mob.skill.impl.Hamstring
import kotlinmud.mob.skill.impl.Meditation
import kotlinmud.mob.skill.impl.SecondAttack
import kotlinmud.mob.skill.impl.Trip
import kotlinmud.mob.skill.impl.benediction.Bless
import kotlinmud.mob.skill.impl.enhancement.GiantStrength
import kotlinmud.mob.skill.impl.evasion.Dodge
import kotlinmud.mob.skill.impl.evasion.Parry
import kotlinmud.mob.skill.impl.evasion.ShieldBlock
import kotlinmud.mob.skill.impl.healing.CureLight
import kotlinmud.mob.skill.impl.healing.CureSerious
import kotlinmud.mob.skill.impl.healing.Heal
import kotlinmud.mob.skill.impl.illusion.Invisibility
import kotlinmud.mob.skill.impl.malediction.Blind
import kotlinmud.mob.skill.impl.malediction.Curse
import kotlinmud.mob.skill.impl.weapon.Dagger
import kotlinmud.mob.skill.impl.weapon.Mace
import kotlinmud.mob.skill.impl.weapon.Staff
import kotlinmud.mob.skill.impl.weapon.Sword
import kotlinmud.mob.skill.impl.weapon.Unarmed
import kotlinmud.mob.skill.impl.weapon.Wand
import kotlinmud.mob.skill.type.Skill

fun createSkillList(): List<Skill> {
    return listOf(
        Bash(),
        Berserk(),
        Bite(),
        ShieldBlock(),
        Parry(),
        Dodge(),
        Disarm(),
        Trip(),
        Hamstring(),
        EnhancedDamage(),
        Meditation(),
        FastHealing(),
        SecondAttack(),

        // weapon
        Dagger(),
        Mace(),
        Staff(),
        Sword(),
        Wand(),
        Unarmed(),

        // healing
        CureLight(),
        CureSerious(),
        Heal(),

        // illusion
        Invisibility(),

        // enhancement
        GiantStrength(),

        // benediction
        Bless(),

        // malediction
        Blind(),
        Curse()
    )
}
