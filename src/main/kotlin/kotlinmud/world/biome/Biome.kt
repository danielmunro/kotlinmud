package kotlinmud.world.biome

import kotlinmud.world.BiomeType
import kotlinmud.world.SubstrateType
import kotlinmud.world.resource.Resource

interface Biome {
    val biomeType: BiomeType
    val resources: Map<Resource, Double>
    val substrate: SubstrateType
    val elevationChange: Double
}
