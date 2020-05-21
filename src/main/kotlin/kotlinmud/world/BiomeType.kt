package kotlinmud.world

enum class BiomeType(val value: String) {
    TUNDRA("tundra"),
    PLAINS("plains"),
    ARBOREAL("arboreal"),
    JUNGLE("jungle"),
    DESERT("desert"),
    MOUNTAIN("mountain"),
    BADLANDS("badlands"),
    NONE("none");

    companion object {
        fun fromString(value: String): BiomeType {
            return valueOf(value.toUpperCase())
        }

        fun getAll(): List<BiomeType> {
            return listOf(
                TUNDRA,
                PLAINS,
                ARBOREAL,
                JUNGLE,
                DESERT,
                MOUNTAIN,
                BADLANDS
            )
        }
    }
}
