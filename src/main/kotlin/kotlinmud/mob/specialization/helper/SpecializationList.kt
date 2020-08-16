package kotlinmud.mob.specialization.helper

import kotlinmud.mob.specialization.impl.Cleric
import kotlinmud.mob.specialization.impl.Mage
import kotlinmud.mob.specialization.impl.Thief
import kotlinmud.mob.specialization.impl.Warrior
import kotlinmud.mob.specialization.type.Specialization

fun createSpecializationList(): List<Specialization> {
    return listOf(
        Mage(),
        Thief(),
        Warrior(),
        Cleric()
    )
}
