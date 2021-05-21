package kotlinmud.respawn.helper

import kotlinmud.attributes.type.Attribute
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.type.Size

fun calculateHpForMob(level: Int, race: Race): Int {
    val sizeModifier = when (race.size) {
        Size.TINY -> 0.5f
        Size.SMALL -> 0.8f
        Size.MEDIUM -> 1.0f
        Size.LARGE -> 1.2f
        Size.HUGE -> 1.4f
    }
    val randomModifier = Math.random()
    return (
        level * (
            (race.attributes[Attribute.CON] ?: 15) * sizeModifier * randomModifier
            ) * (
            (race.attributes[Attribute.STR] ?: 15) * (sizeModifier / 2) * randomModifier
            )
        ).toInt()
}
