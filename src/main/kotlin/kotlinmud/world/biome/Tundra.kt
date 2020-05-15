package kotlinmud.world.biome

import kotlinmud.world.resource.Resource

class Tundra : Biome {
    override val biomeType: BiomeType = BiomeType.TUNDRA
    override val resources: Map<Resource, Double> = mapOf()
}
