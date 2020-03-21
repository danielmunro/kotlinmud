package kotlinmud.math

fun normalizeInt(min: Int, actual: Int, max: Int): Int {
    if (min > actual) {
        return min
    } else if (actual > max) {
        return max
    }
    return actual
}

fun normalizeDouble(min: Double, actual: Double, max: Double): Double {
    if (min > actual) {
        return min
    } else if (actual > max) {
        return max
    }
    return actual
}
