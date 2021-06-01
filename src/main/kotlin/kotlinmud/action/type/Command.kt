package kotlinmud.action.type

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
    QUAFF("quaff"),

    // room owner actions
    OWNER_INFO("owner info"),
    OWNER_SET("owner set"),

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
    SCAN("scan"),

    // crafting
    CRAFT("craft"),
    HARVEST("harvest"),
    RECIPES("recipes"),
    RECIPE_OF("recipe of"),

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
    DISARM("disarm"),
    TRIP("trip"),
    HAMSTRING("hamstring"),
    BACK_STAB("backstab"),
    TAIL("tail"),

    // door
    OPEN("open"),
    CLOSE("close"),
    LOCK("lock"),
    UNLOCK("unlock"),

    // dispositions
    SIT("sit"),
    SLEEP("sleep"),
    WAKE("wake"),
    NOOP("noop"),

    // other
    QUIT("quit"),
    RECALL("recall"),

    // skills
    BASH("bash"),
    BERSERK("berserk"),
    BITE("bite"),
    INVISIBILITY("invisibility"),
    CAST("cast"),

    // resources
    TILL("till"),

    // questing
    QUEST_LOG("quest log"),
    QUEST_LIST("quest list"),
    QUEST_ACCEPT("quest accept"),
    QUEST_ABANDON("quest abandon"),
    QUEST_SUBMIT("quest submit"),

    // player
    DESCRIPTION("description"),

    // admin
    BAN("ban"),
    UNBAN("unban"),

    // spells
    CURE_LIGHT("cure light"),
}
