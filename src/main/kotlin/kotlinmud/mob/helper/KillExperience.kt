package kotlinmud.mob.helper

import kotlinmud.mob.dao.MobDAO

fun getExperienceGain(victor: MobDAO, vanquished: MobDAO): Int {
    val experience = getBaseKillExperience(vanquished.level - victor.level)
    return when {
        victor.level < 11 -> 15 * experience / (victor.level + 4)
        victor.level > 40 -> 40 * experience / (victor.level - 1)
        else -> experience
    }
}

fun getBaseKillExperience(levelDifference: Int): Int {
    return when (levelDifference) {
        -8 -> 2
        -7 -> 7
        -6 -> 13
        -5 -> 20
        -4 -> 26
        -3 -> 40
        -2 -> 60
        -1 -> 80
        0 -> 100
        1 -> 140
        2 -> 180
        3 -> 220
        4 -> 280
        5 -> 320
        else -> {
            if (levelDifference > 5) {
                return 320 + 30 * (levelDifference - 5)
            }
            0
        }
    }
}
