package kotlinmud.helper.string

fun String.matches(name: String): Boolean {
    return name.lowercase().split(" ").any { it.length > 1 && it.startsWith(this.lowercase()) }
}
