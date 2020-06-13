package kotlinmud.helper.random

import java.util.stream.IntStream
import kotlin.random.Random
import kotlin.streams.toList

fun <T> randomAmount(amount: Int, builder: () -> T): List<T> {
    return IntStream.range(0, Random.nextInt(0, amount))
        .mapToObj { builder() }
        .toList()
}
