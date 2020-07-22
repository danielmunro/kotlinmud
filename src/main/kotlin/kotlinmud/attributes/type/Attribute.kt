package kotlinmud.attributes.type

enum class Attribute {
    HP,
    MANA,
    MV,
    STR,
    INT,
    WIS,
    DEX,
    CON,
    HIT,
    DAM,
    AC_BASH,
    AC_SLASH,
    AC_PIERCE,
    AC_MAGIC;

    fun isVitals(): Boolean {
        return this == HP || this == MANA || this == MV
    }
}
