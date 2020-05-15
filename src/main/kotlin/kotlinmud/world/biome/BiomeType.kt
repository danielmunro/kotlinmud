package kotlinmud.world.biome

enum class BiomeType(val value: String) {
    TUNDRA("tundra"),
    PLAINS("plains"),
    ARBOREAL("arboreal"),
    JUNGLE("jungle"),
    DESERT("desert"),
    MOUNTAIN("mountain"),
    BADLANDS("badlands"),
    UPPER_CRUST("upper_crust"),
    LOWER_CRUST("lower_crust"),
    NONE("none");

    companion object {
        fun fromString(value: String): BiomeType {
            return valueOf(value.toUpperCase())
        }
    }
}
