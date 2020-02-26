package kotlinmud.action

enum class Command(val value: String) {
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

    // fighting
    KILL("kill"),
    FLEE("flee"),

    // door
    OPEN("open"),
    CLOSE("close"),

    // dispositions
    SIT("sit"),
    SLEEP("sleep"),
    WAKE("wake"),
    NOOP("noop");

    fun startsWith(input: String): Boolean {
        return value.startsWith(input)
    }
}
