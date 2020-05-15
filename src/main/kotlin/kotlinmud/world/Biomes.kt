package kotlinmud.world

import kotlinmud.world.biome.Arboreal
import kotlinmud.world.biome.Biome
import kotlinmud.world.biome.DefaultBiome
import kotlinmud.world.biome.Plains
import kotlinmud.world.biome.Tundra

fun createBiomes(): List<Biome> {
    return listOf(
        DefaultBiome(),
        Plains(),
        Arboreal(),
        Tundra()
    )
}
