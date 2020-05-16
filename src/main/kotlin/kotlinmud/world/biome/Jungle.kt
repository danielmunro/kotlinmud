package kotlinmud.world.biome

import kotlinmud.world.BiomeType
import kotlinmud.world.SubstrateType
import kotlinmud.world.resource.JungleTree
import kotlinmud.world.resource.Resource

class Jungle : Biome {
    override val biomeType: BiomeType = BiomeType.JUNGLE
    override val resources: Map<Resource, Double> = mapOf(
        Pair(JungleTree(), 0.95)
    )
    override val substrate: SubstrateType = SubstrateType.DIRT
}
