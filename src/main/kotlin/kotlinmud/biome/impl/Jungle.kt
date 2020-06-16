package kotlinmud.biome.impl

import kotlinmud.biome.type.Biome
import kotlinmud.biome.type.BiomeType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.mob.factory.fox
import kotlinmud.mob.factory.turkey
import kotlinmud.world.resource.JungleTree
import kotlinmud.world.resource.Resource

class Jungle : Biome {
    override val biomeType: BiomeType = BiomeType.JUNGLE
    override val resources: Map<Resource, Double> = mapOf(
        Pair(JungleTree(), 0.95)
    )
    override val substrate: SubstrateType = SubstrateType.DIRT
    override val elevationChange: Double = 0.6
    override val mobs = listOf(
        turkey(),
        fox()
    )
}
