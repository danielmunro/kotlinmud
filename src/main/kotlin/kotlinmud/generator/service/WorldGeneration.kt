package kotlinmud.generator.service

import kotlinmud.generator.model.World
import kotlinmud.generator.type.Layer
import kotlinmud.generator.type.Matrix3D

class WorldGeneration {
    var biomeLayer: Layer? = null
    var elevationLayer: Layer? = null
    var matrix: Matrix3D? = null
    var world: World? = null
}
