package kotlinmud.mob.skill.factory

import kotlinmud.mob.skill.type.LearningDifficulty
import kotlinmud.mob.specialization.type.SpecializationType

fun easyForCleric(): Pair<SpecializationType, LearningDifficulty> {
    return Pair(SpecializationType.CLERIC, LearningDifficulty.EASY)
}

fun normalForCleric(): Pair<SpecializationType, LearningDifficulty> {
    return Pair(SpecializationType.CLERIC, LearningDifficulty.NORMAL)
}

fun easyForMage(): Pair<SpecializationType, LearningDifficulty> {
    return Pair(SpecializationType.CLERIC, LearningDifficulty.EASY)
}

fun hardForWarrior(): Pair<SpecializationType, LearningDifficulty> {
    return Pair(SpecializationType.CLERIC, LearningDifficulty.EASY)
}
