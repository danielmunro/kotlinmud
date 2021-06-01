package kotlinmud.helper.math

import kotlin.random.Random

fun d20(): Int {
    return Random.nextInt(1, 21)
}

fun dice(rolls: Int, number: Int): Int {
    return Random.nextInt(1, Math.max(1, number) + 1) +
        if (rolls > 1) dice(rolls - 1, number) else 0
}

fun coinFlip(): Boolean {
    return Random.nextInt(1, 3) == 1
}

fun percentRoll(): Int {
    return Random.nextInt(1, 101)
}
