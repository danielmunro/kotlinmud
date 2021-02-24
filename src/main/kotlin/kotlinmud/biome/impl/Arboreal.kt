package kotlinmud.biome.impl

import kotlinmud.biome.type.Biome
import kotlinmud.biome.type.BiomeType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.mob.model.Mob
import kotlinmud.resource.impl.BlackberryBush
import kotlinmud.resource.impl.PineTree
import kotlinmud.resource.type.Resource
import kotlinmud.room.dao.RoomDAO

class Arboreal : Biome {
    override val biomeType: BiomeType = BiomeType.ARBOREAL
    override val resources: Map<Resource, Double> = mapOf(
        Pair(PineTree(), 0.80),
        Pair(BlackberryBush(), 0.15)
    )
    override val substrate: SubstrateType = SubstrateType.DIRT
    override val elevationChange: Double = 0.45
    override val mobs: List<(RoomDAO) -> Mob> = listOf()
}
