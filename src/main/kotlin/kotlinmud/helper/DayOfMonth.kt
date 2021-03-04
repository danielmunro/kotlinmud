package kotlinmud.helper

fun getEndOfDayMonth(n: Int): String {
    return when (n % 10) {
        1 -> "st"
        2 -> "nd"
        3 -> "rd"
        else -> "th"
    }
}
