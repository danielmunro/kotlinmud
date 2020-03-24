package kotlinmud.math

import kotlin.random.Random

fun d20(): Int {
    return Random.nextInt(1, 20)
}

fun dN(rolls: Int, number: Int): Int {
    return Random.nextInt(1, Math.max(1, number) + 1) +
            if (rolls > 1) dN(rolls - 1, number) else 0
}

fun percentRoll(): Int {
    return Random.nextInt(1, 100)
}
