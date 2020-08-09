package kotlinmud.event.type

enum class EventType {
    MOB_LEAVE_ROOM,
    CLIENT_CONNECTED,
    CLIENT_DISCONNECTED,
    SEND_MESSAGE_TO_ROOM,
    INPUT_RECEIVED,
    PULSE,
    TICK,
    DAY,
    FIGHT_STARTED,
    FIGHT_ROUND,
    KILL,
    SOCIAL,
    CLIENT_LOGGED_IN,
    GAME_START
}
