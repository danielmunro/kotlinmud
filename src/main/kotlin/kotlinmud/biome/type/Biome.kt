package kotlinmud.biome.type

import kotlinmud.mob.dao.MobDAO
import kotlinmud.room.dao.RoomDAO
import kotlinmud.world.resource.Resource

interface Biome {
    val biomeType: BiomeType
    val resources: Map<Resource, Double>
    val substrate: SubstrateType
    val elevationChange: Double
    fun createMobInRoom(room: RoomDAO): MobDAO
}
