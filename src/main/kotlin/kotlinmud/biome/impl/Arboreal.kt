package kotlinmud.biome.impl

import kotlinmud.biome.type.Biome
import kotlinmud.biome.type.BiomeType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.mob.factory.chicken
import kotlinmud.mob.factory.deer
import kotlinmud.mob.factory.fox
import kotlinmud.mob.factory.turkey
import kotlinmud.mob.model.Mob
import kotlinmud.world.resource.PineTree
import kotlinmud.world.resource.Resource

class Arboreal : Biome {
    override val biomeType: BiomeType = BiomeType.ARBOREAL
    override val resources: Map<Resource, Double> = mapOf(
        Pair(PineTree(), 0.80)
    )
    override val substrate: SubstrateType = SubstrateType.DIRT
    override val elevationChange: Double = 0.45
    override val mobs: List<Mob> = listOf(
        deer(),
        turkey(),
        chicken(),
        fox()
    )
}
