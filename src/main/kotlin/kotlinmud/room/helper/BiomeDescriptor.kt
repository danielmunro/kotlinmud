package kotlinmud.room.helper

import kotlinmud.biome.type.BiomeType
import kotlinmud.room.dao.RoomDAO

fun getBiomeLabel(biomeType: BiomeType): String {
    return when (biomeType) {
        BiomeType.TUNDRA -> "tundra"
        BiomeType.PLAINS -> "plains"
        BiomeType.MOUNTAIN -> "mountainous"
        BiomeType.BADLANDS -> "sulfurous swamp"
        BiomeType.JUNGLE -> "jungle"
        BiomeType.DESERT -> "desert"
        BiomeType.ARBOREAL -> "forest"
        BiomeType.SKY -> "flight"
        BiomeType.UNDERGROUND -> "cave"
        BiomeType.NONE -> "ethereal realm"
    }
}

fun getBiomeDescription(room: RoomDAO): String {
    return when (room.biome) {
        BiomeType.ARBOREAL -> "is a forest of evergreen trees."
        BiomeType.DESERT -> "rolling dunes of sand spread out before you."
        BiomeType.JUNGLE -> "is a dense jungle."
        BiomeType.BADLANDS -> "a sulphurous smell permeates rocky and unwelcoming terrain."
        BiomeType.MOUNTAIN -> "is a large mountain of rocks."
        BiomeType.PLAINS -> "is a flat grassland."
        BiomeType.TUNDRA -> "is a cold, flat land."
        BiomeType.UNDERGROUND -> "is surrounded by rock, deep underground."
        BiomeType.SKY -> "is the sky."
        BiomeType.NONE -> "is nether."
    }
}
