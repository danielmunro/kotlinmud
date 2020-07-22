package kotlinmud.biome.impl

import kotlinmud.biome.type.Biome
import kotlinmud.biome.type.BiomeType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.factory.fox
import kotlinmud.mob.factory.polarBear
import kotlinmud.mob.factory.rabbit
import kotlinmud.mob.factory.wolf
import kotlinmud.room.dao.RoomDAO
import kotlinmud.world.resource.Resource

class Tundra : Biome {
    override val biomeType: BiomeType = BiomeType.TUNDRA
    override val resources: Map<Resource, Double> = mapOf()
    override val substrate: SubstrateType = SubstrateType.DIRT
    override val elevationChange: Double = 0.1
    val mobs = listOf(
        { room: RoomDAO -> fox(room) },
        { room: RoomDAO -> wolf(room) },
        { room: RoomDAO -> rabbit(room) },
        { room: RoomDAO -> polarBear(room) }
    )

    override fun createMobInRoom(room: RoomDAO): MobDAO {
        return mobs.random().invoke(room)
    }
}
