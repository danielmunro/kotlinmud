package kotlinmud.biome.impl

import kotlinmud.biome.type.Biome
import kotlinmud.biome.type.BiomeType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.mob.factory.deer
import kotlinmud.mob.factory.horse
import kotlinmud.mob.factory.rabbit
import kotlinmud.mob.factory.sheep
import kotlinmud.room.dao.RoomDAO
import kotlinmud.world.resource.PineTree
import kotlinmud.world.resource.Resource

class Plains : Biome {
    override val biomeType: BiomeType = BiomeType.PLAINS
    override val resources: Map<Resource, Double> = mapOf(
        Pair(PineTree(), 0.05)
    )
    override val substrate: SubstrateType = SubstrateType.DIRT
    override val elevationChange: Double = 0.1
    override val mobs = listOf(
        { room: RoomDAO -> deer(room) },
        { room: RoomDAO -> rabbit(room) },
        { room: RoomDAO -> horse(room) },
        { room: RoomDAO -> sheep(room) }
    )
}
