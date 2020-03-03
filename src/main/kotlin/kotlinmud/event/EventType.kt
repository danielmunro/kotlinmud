package kotlinmud.event

enum class EventType {
    MOB_LEAVE_ROOM,
    CLIENT_CONNECTED,
    SEND_MESSAGE_TO_ROOM,
    INPUT_RECEIVED,
    PULSE,
    TICK,
    FIGHT_ROUND,
    SOCIAL,
}
