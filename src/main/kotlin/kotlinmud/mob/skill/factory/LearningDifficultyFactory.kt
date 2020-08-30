package kotlinmud.mob.skill.factory

import kotlinmud.mob.skill.type.LearningDifficulty
import kotlinmud.mob.specialization.type.SpecializationType

fun easyForCleric(): Pair<SpecializationType, LearningDifficulty> {
    return Pair(SpecializationType.CLERIC, LearningDifficulty.EASY)
}

fun normalForCleric(): Pair<SpecializationType, LearningDifficulty> {
    return Pair(SpecializationType.CLERIC, LearningDifficulty.NORMAL)
}

fun hardForCleric(): Pair<SpecializationType, LearningDifficulty> {
    return Pair(SpecializationType.CLERIC, LearningDifficulty.HARD)
}

fun veryHardForCleric(): Pair<SpecializationType, LearningDifficulty> {
    return Pair(SpecializationType.CLERIC, LearningDifficulty.VERY_HARD)
}

fun easyForMage(): Pair<SpecializationType, LearningDifficulty> {
    return Pair(SpecializationType.CLERIC, LearningDifficulty.EASY)
}

fun normalForMage(): Pair<SpecializationType, LearningDifficulty> {
    return Pair(SpecializationType.MAGE, LearningDifficulty.NORMAL)
}

fun hardForMage(): Pair<SpecializationType, LearningDifficulty> {
    return Pair(SpecializationType.MAGE, LearningDifficulty.HARD)
}

fun veryHardForMage(): Pair<SpecializationType, LearningDifficulty> {
    return Pair(SpecializationType.MAGE, LearningDifficulty.VERY_HARD)
}

fun easyForWarrior(): Pair<SpecializationType, LearningDifficulty> {
    return Pair(SpecializationType.WARRIOR, LearningDifficulty.EASY)
}

fun normalForWarrior(): Pair<SpecializationType, LearningDifficulty> {
    return Pair(SpecializationType.WARRIOR, LearningDifficulty.NORMAL)
}

fun hardForWarrior(): Pair<SpecializationType, LearningDifficulty> {
    return Pair(SpecializationType.CLERIC, LearningDifficulty.HARD)
}

fun easyForThief(): Pair<SpecializationType, LearningDifficulty> {
    return Pair(SpecializationType.THIEF, LearningDifficulty.EASY)
}

fun normalForThief(): Pair<SpecializationType, LearningDifficulty> {
    return Pair(SpecializationType.THIEF, LearningDifficulty.NORMAL)
}

fun hardForThief(): Pair<SpecializationType, LearningDifficulty> {
    return Pair(SpecializationType.THIEF, LearningDifficulty.HARD)
}
