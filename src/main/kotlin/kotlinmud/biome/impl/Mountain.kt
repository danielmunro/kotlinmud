package kotlinmud.biome.impl

import kotlinmud.biome.type.Biome
import kotlinmud.biome.type.BiomeType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.mob.factory.fox
import kotlinmud.mob.factory.goat
import kotlinmud.mob.factory.rabbit
import kotlinmud.mob.factory.wolf
import kotlinmud.mob.model.Mob
import kotlinmud.world.resource.CoalOre
import kotlinmud.world.resource.IronOre
import kotlinmud.world.resource.Resource

class Mountain : Biome {
    override val biomeType: BiomeType = BiomeType.MOUNTAIN
    override val resources: Map<Resource, Double> = mapOf(
        Pair(IronOre(), 0.03),
        Pair(CoalOre(), 0.06)
    )
    override val substrate: SubstrateType = SubstrateType.ROCK
    override val elevationChange: Double = 0.95
    override val mobs: List<Mob> = listOf(
        fox(),
        wolf(),
        goat(),
        rabbit()
    )
}
