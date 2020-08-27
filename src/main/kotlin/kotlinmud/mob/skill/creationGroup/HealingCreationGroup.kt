package kotlinmud.mob.skill.creationGroup

import kotlinmud.mob.skill.impl.healing.CureLight
import kotlinmud.mob.skill.impl.healing.CureSerious
import kotlinmud.mob.skill.impl.healing.Heal
import kotlinmud.mob.skill.type.CreationGroup

class HealingCreationGroup : CreationGroup {
    override val name = "healing"
    override val skills = listOf(
        CureLight(),
        CureSerious(),
        Heal()
    )
    override val points = 8
}
