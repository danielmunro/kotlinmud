package kotlinmud.biome.type

enum class BiomeType(val value: String) {
    TUNDRA("tundra"),
    PLAINS("plains"),
    ARBOREAL("arboreal"),
    JUNGLE("jungle"),
    DESERT("desert"),
    MOUNTAIN("mountain"),
    BADLANDS("badlands"),
    UNDERGROUND("underground"),
    SKY("sky"),
    NONE("none");

    companion object {
        fun fromString(value: String): BiomeType {
            return valueOf(value.toUpperCase())
        }

        fun fromIndex(i: Int): BiomeType {
            return values()[i]
        }
    }

    fun isSurface(): Boolean {
        return this != SKY && this != UNDERGROUND && this != NONE
    }
}
