package kotlinmud.io

enum class Syntax(val syntax: String) {
    COMMAND("command"),
    DIRECTION_TO_EXIT("direction to exit"),
    NOOP("noop"),
}