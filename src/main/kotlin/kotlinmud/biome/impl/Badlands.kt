package kotlinmud.biome.impl

import kotlinmud.biome.type.Biome
import kotlinmud.biome.type.BiomeType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.mob.model.MobBuilder
import kotlinmud.world.resource.Resource

class Badlands : Biome {
    override val biomeType: BiomeType = BiomeType.BADLANDS
    override val resources: Map<Resource, Double> = mapOf()
    override val substrate: SubstrateType = SubstrateType.GRAVEL
    override val elevationChange: Double = 0.3
    override val mobs = listOf<MobBuilder>()
}
