package kotlinmud.action

enum class Command(val command: String) {
    NORTH("north"),
    SOUTH("south"),
    EAST("east"),
    WEST("west"),
    UP("up"),
    DOWN("down"),
    LOOK("look"),
    EXIT("exit"),
    NOOP("noop"),
}