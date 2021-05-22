package kotlinmud.respawn.helper

import kotlinmud.attributes.type.Attribute
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.type.Size

fun calculateHpForMob(level: Int, race: Race): Int {
    val sizeModifier = when (race.size) {
        Size.TINY -> 1.0f
        Size.SMALL -> 1.2f
        Size.MEDIUM -> 2.2f
        Size.LARGE -> 2.9f
        Size.HUGE -> 3.5f
    }
    val randomModifier = 1.0 + Math.random()
    return (
        level * (
            (
                (race.attributes[Attribute.CON] ?: 15) * sizeModifier * randomModifier
                )
            ) * (
            (
                race.attributes[Attribute.STR] ?: 15
                ) * (sizeModifier / 2) * randomModifier
            )
        ).toInt()
}
