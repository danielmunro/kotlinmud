package kotlinmud.biome.impl

import kotlinmud.biome.type.Biome
import kotlinmud.biome.type.BiomeType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.mob.factory.blackBear
import kotlinmud.mob.factory.fox
import kotlinmud.mob.factory.goat
import kotlinmud.mob.factory.rabbit
import kotlinmud.mob.factory.sheep
import kotlinmud.mob.factory.wolf
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
    override val mobs = listOf(
        { room: RoomDAO -> fox(room) },
        { room: RoomDAO -> wolf(room) },
        { room: RoomDAO -> goat(room) },
        { room: RoomDAO -> sheep(room) },
        { room: RoomDAO -> rabbit(room) },
        { room: RoomDAO -> blackBear(room) }
    )
}
