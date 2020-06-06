package kotlinmud.world.generation

import kotlin.random.Random

class BiomeService(private val width: Int, private val length: Int) {
    private val biomeLayer = Array(length) {
        IntArray(width) {
            0
        }
    }

    fun createLayer(biomeCount: Int): Layer {
        val coordinates = mutableListOf<Pair<Int, Int>>()
        for (i in 1..biomeCount) {
            val w = Random.nextInt(0, width)
            val l = Random.nextInt(0, length)
            val biome = GeneratorService.randomBiome()
            biomeLayer[l][w] = biome
            coordinates.add(Pair(l, w))
        }
        var iteration = 0
        while (coordinates.size > 0) {
            coordinates.removeIf {
                !drawAround(
                    Pair(it.first - iteration, it.second - iteration),
                    Pair(it.first + iteration, it.second + iteration),
                    biomeLayer[it.first][it.second]
                )
            }
            iteration++
        }
        return biomeLayer
    }

    private fun drawAround(c1: Pair<Int, Int>, c2: Pair<Int, Int>, value: Int): Boolean {
        var drawn = false
        for (l in c1.first - 1..c2.first) {
            for (w in c1.second - 1..c2.second) {
                if (isInBounds(l, w) && ((w <= c1.first || w >= c2.first) || (l <= c1.second || l >= c2.second)) && biomeLayer[l][w] == 0) {
                    biomeLayer[l][w] = value
                    drawn = true
                }
            }
        }
        return drawn
    }

    private fun isInBounds(x: Int, y: Int): Boolean {
        return x in 0 until length && y in 0 until width
    }
}
