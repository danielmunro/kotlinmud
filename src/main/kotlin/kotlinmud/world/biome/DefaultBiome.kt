package kotlinmud.world.biome

import kotlinmud.world.resource.Resource

class DefaultBiome : Biome {
    override val id: Int = 0
    override val biomeType: BiomeType = BiomeType.NONE
    override val resources: Map<Resource, Double> = mapOf()
}
