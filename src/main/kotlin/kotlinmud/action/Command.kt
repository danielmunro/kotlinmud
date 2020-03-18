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
    EQUIPMENT("equipment"),

    // shops
    LIST("list"),
    BUY("buy"),
    SELL("sell"),

    // informational
    LOOK("look"),
    INVENTORY("inventory"),
    SCORE("score"),
    ATTRIBUTES("attributes"),
    AFFECTS("affects"),
    TIME("time"),
    WEATHER("weather"),

    // mobs
    FOLLOW("follow"),
    GROUP("group"),
    NOFOLLOW("nofollow"),
    GROUP_TELL("group tell"),
    GT("GT"),

    // social
    SAY("say"),
    GOSSIP("gossip"),
    TELL("tell"),

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
