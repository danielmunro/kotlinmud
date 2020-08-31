package kotlinmud.mob.skill.helper

import kotlinmud.mob.skill.creationGroup.ClericDefaultCreationGroup
import kotlinmud.mob.skill.creationGroup.MageDefaultCreationGroup
import kotlinmud.mob.skill.creationGroup.ThiefDefaultCreationGroup
import kotlinmud.mob.skill.creationGroup.WarriorDefaultCreationGroup
import kotlinmud.mob.skill.creationGroup.spell.BenedictionSpellGroup
import kotlinmud.mob.skill.creationGroup.spell.EnhancementSpellGroup
import kotlinmud.mob.skill.creationGroup.spell.HealingSpellGroup
import kotlinmud.mob.skill.creationGroup.spell.IllusionSpellGroup
import kotlinmud.mob.skill.creationGroup.spell.MaledictionSpellGroup
import kotlinmud.mob.skill.type.Customization

fun createCreationGroupList(): List<Customization> {
    return listOf(
        HealingSpellGroup(),
        IllusionSpellGroup(),
        MaledictionSpellGroup(),
        BenedictionSpellGroup(),
        EnhancementSpellGroup(),
        WarriorDefaultCreationGroup(),
        ThiefDefaultCreationGroup(),
        ClericDefaultCreationGroup(),
        MageDefaultCreationGroup()
    ) + getSkillsAsCustomizations()
}

fun getSkillsAsCustomizations(): List<Customization> {
    return createSkillList().filterIsInstance(Customization::class.java)
}
