package kotlinmud.world.biome

import kotlinmud.world.resource.Resource

class Mountain : Biome {
    override val biomeType: BiomeType = BiomeType.MOUNTAIN
    override val resources: Map<Resource, Double> = mapOf()
}
