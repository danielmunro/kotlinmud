package kotlinmud.world.biome

import kotlinmud.world.resource.Resource

class Badlands : Biome {
    override val biomeType: BiomeType = BiomeType.BADLANDS
    override val resources: Map<Resource, Double> = mapOf()
}
