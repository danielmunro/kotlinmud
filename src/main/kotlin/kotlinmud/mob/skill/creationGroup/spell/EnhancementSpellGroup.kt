package kotlinmud.mob.skill.creationGroup.spell

import kotlinmud.mob.skill.impl.enhancement.GiantStrength
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.SpellGroup

class EnhancementSpellGroup : SpellGroup, Customization {
    override val name = "enhancement"
    override val spells = listOf(
        GiantStrength()
    )
    override val points: Int = 8
    override val creationGroupType = CreationGroupType.SPELL_GROUP
}
