package kotlinmud.mob.fight.type

enum class DamageType(private val value: String) {
    SLASH("slash"),
    POUND("pound"),
    PIERCE("pierce"),
    FIRE("fire"),
    COLD("cold"),
    LIGHTNING("lightning"),
    ACID("acid"),
    POISON("poison"),
    NEGATIVE("negative"),
    HOLY("holy"),
    ENERGY("energy"),
    MENTAL("mental"),
    DISEASE("disease"),
    DROWNING("drowning"),
    LIGHT("light"),
    SOUND("sound"),
    NONE("none")
}
