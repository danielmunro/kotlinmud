package kotlinmud.world.biome

import kotlinmud.world.BiomeType
import kotlinmud.world.SubstrateType
import kotlinmud.world.resource.PineTree
import kotlinmud.world.resource.Resource

class Arboreal : Biome {
    override val biomeType: BiomeType = BiomeType.ARBOREAL
    override val resources: Map<Resource, Double> = mapOf(
        Pair(PineTree(), 0.80)
    )
    override val substrate: SubstrateType = SubstrateType.DIRT
}
