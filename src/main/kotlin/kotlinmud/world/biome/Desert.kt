package kotlinmud.world.biome

import kotlinmud.world.BiomeType
import kotlinmud.world.SubstrateType
import kotlinmud.world.resource.Brush
import kotlinmud.world.resource.Resource
import kotlinmud.world.resource.Tar

class Desert : Biome {
    override val biomeType: BiomeType = BiomeType.DESERT
    override val resources: Map<Resource, Double> = mapOf(
        Pair(Tar(), 0.03),
        Pair(Brush(), 0.05)
    )
    override val substrate: SubstrateType = SubstrateType.SAND
}
