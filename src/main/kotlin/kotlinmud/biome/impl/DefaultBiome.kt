package kotlinmud.biome.impl

import kotlinmud.biome.type.Biome
import kotlinmud.biome.type.BiomeType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.mob.model.MobBuilder
import kotlinmud.world.resource.Resource

class DefaultBiome : Biome {
    override val biomeType: BiomeType = BiomeType.NONE
    override val resources: Map<Resource, Double> = mapOf()
    override val substrate: SubstrateType = SubstrateType.DIRT
    override val elevationChange: Double = 0.2
    override val mobs = listOf<MobBuilder>()
}
