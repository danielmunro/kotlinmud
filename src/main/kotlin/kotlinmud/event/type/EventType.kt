package kotlinmud.event.type

enum class EventType {
    CLIENT_CONNECTED,
    CLIENT_DISCONNECTED,
    SEND_MESSAGE_TO_ROOM,
    PULSE,
    TICK,
    REGEN,
    DAY,
    FIGHT_STARTED,
    FIGHT_ROUND,
    KILL,
    SOCIAL,
    CLIENT_LOGGED_IN,
    GAME_START,
    GAME_LOOP,
    TILL
}
