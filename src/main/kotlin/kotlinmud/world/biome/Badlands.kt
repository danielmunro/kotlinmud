package kotlinmud.world.biome

import kotlinmud.world.BiomeType
import kotlinmud.world.SubstrateType
import kotlinmud.world.resource.Resource

class Badlands : Biome {
    override val biomeType: BiomeType = BiomeType.BADLANDS
    override val resources: Map<Resource, Double> = mapOf()
    override val substrate: SubstrateType = SubstrateType.GRAVEL
}
