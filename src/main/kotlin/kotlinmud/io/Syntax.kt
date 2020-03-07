package kotlinmud.io

enum class Syntax(val syntax: String) {
    COMMAND("command"),
    DIRECTION_TO_EXIT("direction to exit"),
    ITEM_IN_ROOM("item in room"),
    ITEM_IN_INVENTORY("item in inventory"),
    EQUIPPED_ITEM("equipped item"),
    EQUIPMENT_IN_INVENTORY("equipment in inventory"),
    ITEM_FROM_MERCHANT("item from merchant"),
    ITEM_TO_SELL("item to sell"),
    MOB_IN_ROOM("mob in room"),
    AVAILABLE_NOUN("available noun"),
    TARGET_MOB("target mob"),
    DOOR_IN_ROOM("door in room"),
    FREE_FORM("free form"),
    NOOP("noop"),
}
