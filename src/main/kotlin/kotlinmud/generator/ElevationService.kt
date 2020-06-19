package kotlinmud.generator

import kotlin.random.Random
import kotlinmud.biome.type.Biome
import kotlinmud.generator.type.Layer

class ElevationService(
    private val biomeLayer: Layer,
    private val biomes: List<Biome>
) {
    fun buildLayer(): Layer {
        val elevationLayer = Array(biomeLayer.size) {
            IntArray(biomeLayer[0].size) {
                0
            }
        }

        for (i in 0..15) {
            for (y in biomeLayer.indices) {
                for (x in biomeLayer[y].indices) {
                    val biome = biomes[biomeLayer[y][x]]
                    if (Random.nextDouble() < biome.elevationChange) {
                        elevationLayer[y][x] += 1
                    }
                }
            }
        }

        for (i in 0..3) {
            smooth(elevationLayer)
        }

        return elevationLayer
    }

    private fun smooth(layer: Layer) {
        for (y in layer.indices) {
            for (x in layer[y].indices) {
                val biome = biomes[biomeLayer[y][x]]
                if (Random.nextDouble() > biome.elevationChange) {
                    val top = y - 1
                    val bottom = y + 1
                    val left = x - 1
                    val right = x + 1
                    val centerValue = layer[y][x]
                    val topValue = if (isInBounds(x, top)) layer[top][x] else centerValue
                    val leftValue = if (isInBounds(left, y)) layer[y][left] else centerValue
                    val rightValue = if (isInBounds(right, y)) layer[y][right] else centerValue
                    val bottomValue = if (isInBounds(x, bottom)) layer[bottom][x] else centerValue
                    val avg = (topValue + leftValue + rightValue + bottomValue) / 4
                    if (avg > centerValue + 1) {
                        layer[y][x] += 1
                    } else if (avg < centerValue - 1) {
                        layer[y][x] -= 1
                    }
                }
            }
        }
    }

    private fun isInBounds(x: Int, y: Int): Boolean {
        return y in biomeLayer.indices &&
                x in biomeLayer[y].indices
    }
}
