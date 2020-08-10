package kotlinmud.mob.skill.helper

import kotlinmud.mob.skill.type.LearningDifficulty

fun getLearningDifficultyPracticeAmount(difficulty: LearningDifficulty): Int {
    return when (difficulty) {
        LearningDifficulty.EASY -> 10
        LearningDifficulty.NORMAL -> 6
        LearningDifficulty.HARD -> 3
        LearningDifficulty.VERY_HARD -> 1
    }
}
