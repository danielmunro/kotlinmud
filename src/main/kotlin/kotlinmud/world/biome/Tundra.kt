package kotlinmud.world.biome

import kotlinmud.world.BiomeType
import kotlinmud.world.SubstrateType
import kotlinmud.world.resource.Resource

class Tundra : Biome {
    override val biomeType: BiomeType = BiomeType.TUNDRA
    override val resources: Map<Resource, Double> = mapOf()
    override val substrate: SubstrateType = SubstrateType.DIRT
}
