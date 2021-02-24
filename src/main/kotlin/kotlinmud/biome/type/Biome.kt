package kotlinmud.biome.type

import kotlinmud.mob.model.Mob
import kotlinmud.resource.type.Resource
import kotlinmud.room.dao.RoomDAO

interface Biome {
    val biomeType: BiomeType
    val resources: Map<Resource, Double>
    val substrate: SubstrateType
    val elevationChange: Double
    val mobs: List<(RoomDAO) -> Mob>
}
