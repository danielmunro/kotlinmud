package kotlinmud.biome.impl

import kotlinmud.biome.type.Biome
import kotlinmud.biome.type.BiomeType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.mob.model.Mob
import kotlinmud.resource.impl.Tar
import kotlinmud.resource.impl.WildGrass
import kotlinmud.resource.type.Resource
import kotlinmud.room.model.Room

class Desert : Biome {
    override val biomeType: BiomeType = BiomeType.DESERT
    override val resources: Map<Resource, Double> = mapOf(
        Pair(Tar(), 0.03),
        Pair(WildGrass(), 0.05)
    )
    override val substrate: SubstrateType = SubstrateType.SAND
    override val elevationChange: Double = 0.1
    override val mobs: List<(Room) -> Mob> = listOf()
}
