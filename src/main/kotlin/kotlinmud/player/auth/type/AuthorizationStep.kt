package kotlinmud.player.auth.type

enum class AuthorizationStep {
    EMAIL,
    PASSWORD,
    MOB_SELECT,
    COMPLETE,
    NEW_MOB,
    RACE_SELECT,
    SPECIALIZATION_SELECT,
    ASK_CUSTOMIZE,
    CUSTOMIZE,
}
