package kotlinmud.mob.skill.creationGroup.spell

import kotlinmud.mob.skill.impl.illusion.Invisibility
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.SpellGroup

class IllusionSpellGroup : SpellGroup, Customization {
    override val name = "illusion"
    override val spells = listOf(
        Invisibility()
    )
    override val points = 8
    override val creationGroupType = CreationGroupType.SPELL_GROUP
}
