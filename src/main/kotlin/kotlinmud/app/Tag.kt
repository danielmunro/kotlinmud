package kotlinmud.app

enum class Tag {
    LOGOUT_ALL_PLAYERS_ON_STARTUP,
    PROCEED_FIGHTS,
    CLIENT_CONNECTED,
    SEND_MESSAGE_TO_ROOM,
    LOG_PLAYER_IN,
    LOG_PLAYER_OUT,
    SOCIAL,
    DECREMENT_AFFECT_TIMEOUT,
    PRUNE_DEAD_MOBS,
    DECREMENT_DELAY,
    DECREMENT_ITEM_DECAY_TIMER,
    DECREASE_HUNGER_AND_THIRST,
    LOG_TICK,
    CHANGE_WEATHER,
    REGEN_MOBS,
    MOVE_MOBS_ON_TICK,
    SCAVENGER_COLLECTS_ITEM,
    GENERATE_MOBS,
    FAST_HEALING,
    MEDITATION,
    GUARD_ATTACKS_AGGRO_MOBS,
    WIMPY,
    ENHANCED_DAMAGE,
    SECOND_ATTACK,
    THIRD_ATTACK,
    GRANT_EXPERIENCE_ON_KILL,
    TRANSFER_GOLD_ON_KILL,
    GROW_RESOURCES,
    GENERATE_GRASS,
    PROCESS_CLIENT_BUFFERS,
    READ_INTO_CLIENT_BUFFERS,
    REMOVE_DISCONNECTED_CLIENTS,
    TIME_SERVICE_LOOP,
    TILL_ROOM,

    QUEST_RECRUIT_PRAETORIAN_GUARD,
}