package kotlinmud.world.biome

import kotlinmud.world.BiomeType
import kotlinmud.world.SubstrateType
import kotlinmud.world.resource.Resource

class DefaultBiome : Biome {
    override val biomeType: BiomeType = BiomeType.NONE
    override val resources: Map<Resource, Double> = mapOf()
    override val substrate: SubstrateType = SubstrateType.DIRT
}
