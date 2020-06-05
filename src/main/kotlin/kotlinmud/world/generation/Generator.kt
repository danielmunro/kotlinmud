package kotlinmud.world.generation

import kotlin.random.Random.Default.nextInt
import kotlinmud.room.model.Room
import kotlinmud.room.type.RegenLevel
import kotlinmud.world.BiomeType

typealias Blocks = Array<Array<IntArray>>

typealias Layer = Array<IntArray>

const val DEPTH = 100

class Generator(private val width: Int, private val length: Int) {
    val biomeLayer = Array(length) {
        IntArray(width) {
            0
        }
    }
    companion object {
        fun randomBiome(): Int {
            val biomeType = BiomeType.getAll().random()
            return BiomeType.values().indexOf(biomeType)
        }
    }

    fun generate(): World {
        var id = 0
        val rooms = mutableListOf<Room>()
        val biomeLayer = createBiomeLayer((width * length) / (width * length / 10))
        return World(rooms, Array(DEPTH) { z ->
            Array(length) { y ->
                IntArray(width) { x ->
                    rooms.add(
                        Room(
                            id,
                            "area",
                            "name",
                            "description",
                            RegenLevel.NORMAL,
                            true,
                            when {
                                z < 40 -> BiomeType.UNDERGROUND
                                z < 60 -> BiomeType.fromIndex(biomeLayer[y][x])
                                else -> BiomeType.SKY
                            },
                            mutableListOf(),
                            mutableListOf(),
                            null
                        )
                    )
                    id++
                }
            }
        })
    }

//    private fun generateLayerRooms(layer: Int, x: Int, y: Int, z: Int):  {
//
//    }

    fun createElevationLayer(biomeLayer: Layer) {

        biomeLayer.forEach { row ->
            row.forEach {
            }
        }
    }

    fun createBiomeLayer(biomeCount: Int): Layer {
        val coordinates = mutableListOf<Pair<Int, Int>>()
        for (i in 1..biomeCount) {
            val w = nextInt(0, width)
            val l = nextInt(0, length)
            val biome = randomBiome()
            biomeLayer[l][w] = biome
            coordinates.add(Pair(l, w))
        }
        var filling = true
        var iteration = 0
        while (filling) {
            println("filling loop, iteration: $iteration")
            coordinates.removeIf {
                val drawn = drawAround(
                    Pair(it.first - iteration, it.second - iteration),
                    Pair(it.first + iteration, it.second + iteration),
                    biomeLayer[it.first][it.second]
                )
                drawn == 0
            }
            iteration++
            filling = false
            var unfilled = 0
            for (l in 0 until length) {
                for (w in 0 until width) {
                    if (biomeLayer[l][w] == 0) {
                        filling = true
                        unfilled += 1
                    }
                }
            }
            println("unfilled: $unfilled")
        }
        return biomeLayer
    }

    private fun drawAround(c1: Pair<Int, Int>, c2: Pair<Int, Int>, value: Int): Int {
//        println("drawAround: $c1, $c2, $value")
        var drawn = 0
        for (w in c1.first - 1..c2.first) {
            for (l in c1.second - 1..c2.second) {
                if (isInBounds(l, w) && ((w <= c1.first || w >= c2.first) || (l <= c1.second || l >= c2.second)) && biomeLayer[l][w] == 0) {
//                    println("filling w: $w, l: $l, value: $value")
                    biomeLayer[l][w] = value
                    drawn++
                }
            }
        }
//        println("---")
        return drawn
    }

    private fun isInBounds(l: Int, w: Int): Boolean {
        return l in 0 until length && w in 0 until width
    }
}
