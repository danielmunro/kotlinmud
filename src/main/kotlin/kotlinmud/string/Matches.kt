package kotlinmud.string

fun matches(name: String, input: String): Boolean {
    return name.split(" ").any { it.length > 1 && it.toLowerCase().startsWith(input.toLowerCase()) }
}
