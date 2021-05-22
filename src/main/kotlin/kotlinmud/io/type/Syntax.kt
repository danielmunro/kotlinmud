package kotlinmud.io.type

enum class Syntax(val syntax: String) {
    COMMAND("command"),
    SUBCOMMAND("subcommand"),
    RECIPE("recipe"),
    DIRECTION_TO_EXIT("direction to exit"),
    DIRECTION_WITH_NO_EXIT("direction with no exit"),
    ITEM_IN_ROOM("item in room"),
    ITEM_IN_INVENTORY("item in inventory"),
    AVAILABLE_ITEM_INVENTORY("available item inventory"),
    ITEM_IN_AVAILABLE_INVENTORY("item"),
    EQUIPPED_ITEM("equipped item"),
    EQUIPMENT_IN_INVENTORY("equipment in inventory"),
    SPELL_FROM_HEALER("spell from healer"),
    ITEM_FROM_MERCHANT("item from merchant"),
    ITEM_TO_SELL("item to sell"),
    RESOURCE_IN_ROOM("resource in room"),
    MOB_IN_ROOM("mob in room"),
    AVAILABLE_NOUN("available noun"),
    OPTIONAL_TARGET("optional target"),
    DOOR_IN_ROOM("door in room"),
    FREE_FORM("free form"),
    SPELL("spell"),
    PLAYER_MOB("player mob"),
    AVAILABLE_DRINK("available drink"),
    AVAILABLE_FOOD("available food"),
    AVAILABLE_POTION("available potion"),
    TRAINABLE("trainable"),
    SKILL_TO_PRACTICE("skill to practice"),
    OPTIONAL_FURNITURE("optional furniture"),
    ACCEPTED_QUEST("accepted quest"),
    AVAILABLE_QUEST("available quest"),
    SUBMITTABLE_QUEST("submittable quest"),
    NOOP("noop"),
}
