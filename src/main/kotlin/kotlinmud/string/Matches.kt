package kotlinmud.string

fun matches(name: String, input: String): Boolean {
    return name.toLowerCase().split(" ").any { it.length > 1 && it.startsWith(input.toLowerCase()) }
}
