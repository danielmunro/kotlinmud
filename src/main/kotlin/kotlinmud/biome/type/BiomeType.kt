package kotlinmud.biome.type

enum class BiomeType {
    TUNDRA,
    PLAINS,
    ARBOREAL,
    JUNGLE,
    DESERT,
    MOUNTAIN,
    BADLANDS,
    UNDERGROUND,
    SKY,
    NONE;

    companion object {
        fun fromIndex(i: Int): BiomeType {
            return values()[i]
        }
    }
}
