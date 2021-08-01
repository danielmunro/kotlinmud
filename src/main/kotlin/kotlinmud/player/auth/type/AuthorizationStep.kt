package kotlinmud.player.auth.type

enum class AuthorizationStep {
    EMAIL,
    PASSWORD,
    ACCOUNT_NAME,
    MOB_SELECT,
    COMPLETE,
    NEW_MOB,
    RACE_SELECT,
    SPECIALIZATION_SELECT,
    GENDER_SELECT,
    ASK_CUSTOMIZE,
    CUSTOMIZE,
}
