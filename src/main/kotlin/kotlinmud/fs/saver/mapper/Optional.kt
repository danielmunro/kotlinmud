package kotlinmud.fs.saver.mapper

fun optional(condition: Boolean, output: String): String {
    return if (condition) "$output, " else ""
}
