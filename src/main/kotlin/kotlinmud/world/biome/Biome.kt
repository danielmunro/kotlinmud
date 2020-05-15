package kotlinmud.world.biome

import kotlinmud.world.resource.Resource

interface Biome {
    val biomeType: BiomeType
    val resources: Map<Resource, Double>
}
