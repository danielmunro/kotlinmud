package kotlinmud.biome.impl

import kotlinmud.biome.type.Biome
import kotlinmud.biome.type.BiomeType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.mob.factory.fox
import kotlinmud.mob.factory.ocelot
import kotlinmud.mob.factory.turkey
import kotlinmud.resource.impl.BlackberryBush
import kotlinmud.resource.impl.JungleTree
import kotlinmud.resource.type.Resource
import kotlinmud.room.dao.RoomDAO

class Jungle : Biome {
    override val biomeType: BiomeType = BiomeType.JUNGLE
    override val resources: Map<Resource, Double> = mapOf(
        Pair(JungleTree(), 0.95),
        Pair(BlackberryBush(), 0.25)
    )
    override val substrate: SubstrateType = SubstrateType.DIRT
    override val elevationChange: Double = 0.6
    override val mobs = listOf(
        { room: RoomDAO -> turkey(room) },
        { room: RoomDAO -> fox(room) },
        { room: RoomDAO -> ocelot(room) }
    )
}
