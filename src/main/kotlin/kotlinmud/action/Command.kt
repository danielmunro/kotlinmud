package kotlinmud.action

enum class Command(private val value: String) {
    NORTH("north"),
    SOUTH("south"),
    EAST("east"),
    WEST("west"),
    UP("up"),
    DOWN("down"),
    LOOK("look"),
    EXIT("exit"),
    GET("get"),
    DROP("drop"),
    INVENTORY("inventory"),
    KILL("kill"),
    NOOP("noop");

    fun startsWith(input: String): Boolean {
        return value.startsWith(input)
    }
}
