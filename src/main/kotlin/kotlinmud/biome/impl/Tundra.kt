package kotlinmud.biome.impl

import kotlinmud.biome.type.Biome
import kotlinmud.biome.type.BiomeType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.mob.model.Mob
import kotlinmud.resource.type.Resource
import kotlinmud.room.model.Room

class Tundra : Biome {
    override val biomeType: BiomeType = BiomeType.TUNDRA
    override val resources: Map<Resource, Double> = mapOf()
    override val substrate: SubstrateType = SubstrateType.DIRT
    override val elevationChange: Double = 0.1
    override val mobs: List<(Room) -> Mob> = listOf()
}
