package kotlinmud.mob.skill.helper

import kotlinmud.mob.skill.creationGroup.ClericDefaultCreationGroup
import kotlinmud.mob.skill.creationGroup.MageDefaultCreationGroup
import kotlinmud.mob.skill.creationGroup.ThiefDefaultCreationGroup
import kotlinmud.mob.skill.creationGroup.WarriorDefaultCreationGroup
import kotlinmud.mob.skill.creationGroup.spell.HealingSpellGroup
import kotlinmud.mob.skill.creationGroup.spell.IllusionSpellGroup
import kotlinmud.mob.skill.creationGroup.spell.MaledictionSpellGroup
import kotlinmud.mob.skill.impl.Bash
import kotlinmud.mob.skill.impl.Berserk
import kotlinmud.mob.skill.impl.Dodge
import kotlinmud.mob.skill.impl.Parry
import kotlinmud.mob.skill.impl.ShieldBlock
import kotlinmud.mob.skill.impl.weapon.Dagger
import kotlinmud.mob.skill.impl.weapon.Mace
import kotlinmud.mob.skill.impl.weapon.Sword
import kotlinmud.mob.skill.impl.weapon.Wand
import kotlinmud.mob.skill.type.Customization

fun createCreationGroupList(): List<Customization> {
    return listOf(
        HealingSpellGroup(),
        IllusionSpellGroup(),
        MaledictionSpellGroup(),
        Bash(),
        Berserk(),
        Dodge(),
        Parry(),
        ShieldBlock(),
        Sword(),
        Mace(),
        Dagger(),
        Wand(),
        WarriorDefaultCreationGroup(),
        ThiefDefaultCreationGroup(),
        ClericDefaultCreationGroup(),
        MageDefaultCreationGroup()
    )
}
