package kotlinmud.mob.skill.creationGroup.spell

import kotlinmud.mob.skill.impl.healing.CureLight
import kotlinmud.mob.skill.impl.healing.CureSerious
import kotlinmud.mob.skill.impl.healing.Heal
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.SpellGroup

class HealingSpellGroup : SpellGroup, Customization {
    override val name = "healing"
    override val spells = listOf(
        CureLight(),
        CureSerious(),
        Heal()
    )
    override val points = 8
    override val creationGroupType = CreationGroupType.SPELL_GROUP
}
