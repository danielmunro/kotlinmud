package kotlinmud.mob.skill.factory

import kotlinmud.mob.specialization.type.SpecializationType

typealias LevelAt = Pair<SpecializationType, Int>

fun clericAt(level: Int): LevelAt {
    return Pair(SpecializationType.CLERIC, level)
}

fun mageAt(level: Int): LevelAt {
    return Pair(SpecializationType.MAGE, level)
}

fun warriorAt(level: Int): LevelAt {
    return Pair(SpecializationType.WARRIOR, level)
}

fun thiefAt(level: Int): LevelAt {
    return Pair(SpecializationType.THIEF, level)
}
