package kotlinmud.world.biome

import kotlinmud.world.resource.Resource

class DefaultBiome : Biome {
    override val biomeType: BiomeType = BiomeType.NONE
    override val resources: Map<Resource, Double> = mapOf()
}
