package kotlinmud.biome.impl

import kotlinmud.biome.type.Biome
import kotlinmud.biome.type.BiomeType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.factory.skeletonWarrior
import kotlinmud.mob.factory.zombie
import kotlinmud.room.dao.RoomDAO
import kotlinmud.world.resource.Resource

class Badlands : Biome {
    override val biomeType: BiomeType = BiomeType.BADLANDS
    override val resources: Map<Resource, Double> = mapOf()
    override val substrate: SubstrateType = SubstrateType.GRAVEL
    override val elevationChange: Double = 0.3
    val mobs = listOf(
        { room: RoomDAO -> zombie(room) },
        { room: RoomDAO -> skeletonWarrior(room) }
    )

    override fun createMobInRoom(room: RoomDAO): MobDAO {
        return mobs.random().invoke(room)
    }
}
