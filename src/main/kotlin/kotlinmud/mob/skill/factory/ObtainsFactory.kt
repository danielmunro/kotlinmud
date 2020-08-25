package kotlinmud.mob.skill.factory

import kotlinmud.mob.specialization.type.SpecializationType

fun clericAt(level: Int): Pair<SpecializationType, Int> {
    return Pair(SpecializationType.CLERIC, level)
}
