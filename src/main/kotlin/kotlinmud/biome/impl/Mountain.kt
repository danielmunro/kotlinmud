package kotlinmud.biome.impl

import kotlinmud.biome.type.Biome
import kotlinmud.biome.type.BiomeType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.mob.model.Mob
import kotlinmud.resource.impl.BlackberryBush
import kotlinmud.resource.impl.CoalOre
import kotlinmud.resource.impl.DiamondOre
import kotlinmud.resource.impl.IronOre
import kotlinmud.resource.type.Resource
import kotlinmud.room.dao.RoomDAO

class Mountain : Biome {
    override val biomeType: BiomeType = BiomeType.MOUNTAIN
    override val resources: Map<Resource, Double> = mapOf(
        Pair(IronOre(), 0.05),
        Pair(CoalOre(), 0.06),
        Pair(BlackberryBush(), 0.05),
        Pair(DiamondOre(), 0.03)
    )
    override val substrate: SubstrateType = SubstrateType.ROCK
    override val elevationChange: Double = 0.95
    override val mobs: List<(RoomDAO) -> Mob> = listOf()
}
