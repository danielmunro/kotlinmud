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
    DRINK("drink"),
    EAT("eat"),
    PUT("put"),

    // room owner actions
    OWNER_INFO("owner info"),
    OWNER_SET("owner set"),

    // room creation/manipulation
    ROOM_NEW("room new"),
    ROOM_BUILD("room build"),
    ROOM_DESCRIPTION("room description"),
    ROOM_INFO("room info"),

    // world state
    SAVE_WORLD("save world"),

    // shops
    LIST("list"),
    BUY("buy"),
    SELL("sell"),
    HEAL("heal"),

    // informational
    LOOK("look"),
    INVENTORY("inventory"),
    SCORE("score"),
    WHO("who"),
    ATTRIBUTES("attributes"),
    AFFECTS("affects"),
    TIME("time"),
    WEATHER("weather"),
    EXITS("exits"),

    // crafting
    CRAFT("craft"),
    HARVEST("harvest"),

    // mobs
    FOLLOW("follow"),
    GROUP("group"),
    NOFOLLOW("nofollow"),
    GROUP_TELL("group tell"),
    GT("GT"),
    TRAIN("train"),
    PRACTICE("practice"),

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
    EXIT("exit"),
    QUIT("quit"),
    RECALL("recall"),

    // skills
    BASH("bash"),
    BERSERK("berserk"),
    BITE("bite"),
    DODGE("dodge"),
    PARRY("parry"),
    SHIELD_BLOCK("shield block"),
    INVISIBILITY("invisibility"),
    ;
}
