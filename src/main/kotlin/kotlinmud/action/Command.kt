package kotlinmud.action

enum class Command(val value: String) {
    // move
    NORTH("north"),
    SOUTH("south"),
    EAST("east"),
    WEST("west"),
    UP("up"),
    DOWN("down"),

    // items
    GET("get"),
    DROP("drop"),
    WEAR("wear"),
    REMOVE("remove"),

    // shops
    LIST("list"),
    BUY("buy"),
    SELL("sell"),

    // informational
    LOOK("look"),
    INVENTORY("inventory"),

    // social
    SAY("say"),

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
    NOOP("noop"),

    // other
    EXIT("exit");
}
