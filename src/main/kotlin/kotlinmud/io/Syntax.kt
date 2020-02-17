package kotlinmud.io

enum class Syntax(val syntax: String) {
    COMMAND("command"),
    DIRECTION_TO_EXIT("direction to exit"),
    ITEM_IN_ROOM("item in room"),
    ITEM_IN_INVENTORY("item in inventory"),
    MOB_IN_ROOM("mob in room"),
    AVAILABLE_NOUN("available noun"),
    NOOP("noop"),
}
