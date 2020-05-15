package kotlinmud.world

import kotlinmud.world.biome.Arboreal
import kotlinmud.world.biome.Badlands
import kotlinmud.world.biome.Biome
import kotlinmud.world.biome.DefaultBiome
import kotlinmud.world.biome.Desert
import kotlinmud.world.biome.Jungle
import kotlinmud.world.biome.Mountain
import kotlinmud.world.biome.Plains
import kotlinmud.world.biome.Tundra

fun createBiomes(): List<Biome> {
    return listOf(
        DefaultBiome(),
        Plains(),
        Arboreal(),
        Tundra(),
        Desert(),
        Jungle(),
        Mountain(),
        Badlands()
    )
}
