package kotlinmud.mob

enum class Disposition(val disposition: String) {
    DEAD("dead"),
    SITTING("sitting"),
    STANDING("standing"),
    FIGHTING("fighting");

    fun toLower(): String {
        return disposition.toLowerCase()
    }
}