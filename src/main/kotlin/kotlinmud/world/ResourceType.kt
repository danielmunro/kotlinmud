package kotlinmud.world

enum class ResourceType(val value: String) {
    BRUSH("brush"),
    COAL_ORE("coal ore"),
    GOLD_ORE("gold ore"),
    IRON_ORE("iron ore"),
    JUNGLE_TREE("jungle tree"),
    PINE_TREE("pine tree"),
    TAR("tar"),
    CLAY("clay");

    companion object {
        fun fromString(value: String): ResourceType {
            return valueOf(value.toUpperCase().replace(" ", "_"))
        }
    }
}
