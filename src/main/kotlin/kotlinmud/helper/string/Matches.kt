package kotlinmud.helper.string

fun String.matches(name: String): Boolean {
    return name.toLowerCase().split(" ").any { it.length > 1 && it.startsWith(this.toLowerCase()) }
}
