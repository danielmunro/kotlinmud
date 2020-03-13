package kotlinmud.math

fun normalize(min: Int, actual: Int, max: Int): Int {
    if (min > actual) {
        return min
    } else if (actual > max) {
        return max
    }
    return actual
}
