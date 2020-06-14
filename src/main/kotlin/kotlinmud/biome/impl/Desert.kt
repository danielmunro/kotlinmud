package kotlinmud.biome.impl

import kotlinmud.biome.type.Biome
import kotlinmud.biome.type.BiomeType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.mob.factory.fox
import kotlinmud.mob.factory.rabbit
import kotlinmud.mob.model.Mob
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
    override val elevationChange: Double = 0.1
    override val mobs: List<Mob> = listOf(
        fox(),
        rabbit()
    )
}
