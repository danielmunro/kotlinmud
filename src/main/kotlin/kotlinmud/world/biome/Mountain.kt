package kotlinmud.world.biome

import kotlinmud.world.BiomeType
import kotlinmud.world.SubstrateType
import kotlinmud.world.resource.CoalOre
import kotlinmud.world.resource.IronOre
import kotlinmud.world.resource.Resource

class Mountain : Biome {
    override val biomeType: BiomeType = BiomeType.MOUNTAIN
    override val resources: Map<Resource, Double> = mapOf(
        Pair(IronOre(), 0.03),
        Pair(CoalOre(), 0.06)
    )
    override val substrate: SubstrateType = SubstrateType.ROCK
    override val elevationChange: Double = 0.95
}
