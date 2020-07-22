package kotlinmud.biome.impl

import kotlinmud.biome.type.Biome
import kotlinmud.biome.type.BiomeType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.factory.fox
import kotlinmud.mob.factory.lizard
import kotlinmud.mob.factory.rabbit
import kotlinmud.room.dao.RoomDAO
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
    val mobs = listOf(
        { room: RoomDAO -> fox(room) },
        { room: RoomDAO -> rabbit(room) },
        { room: RoomDAO -> lizard(room) }
    )

    override fun createMobInRoom(room: RoomDAO): MobDAO {
        return mobs.random().invoke(room)
    }
}
