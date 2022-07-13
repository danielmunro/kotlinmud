package kotlinmud.utility

fun changeLine(description: String, substitute: String, lineNumber: Int): String {
    val lines = description.split("\n").toMutableList()
    lines[lineNumber] = substitute
    return lines.joinToString("\n")
}

fun removeLine(description: String, lineNumber: Int): String {
    val lines = description.split("\n").toMutableList()
    lines.removeAt(lineNumber)
    return lines.joinToString("\n")
}
