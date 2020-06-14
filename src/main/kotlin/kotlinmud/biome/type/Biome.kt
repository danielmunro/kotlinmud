package kotlinmud.biome.type

import kotlinmud.mob.model.Mob
import kotlinmud.world.resource.Resource

interface Biome {
    val biomeType: BiomeType
    val resources: Map<Resource, Double>
    val substrate: SubstrateType
    val elevationChange: Double
    val mobs: List<Mob>
}
