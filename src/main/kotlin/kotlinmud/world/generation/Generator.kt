package kotlinmud.world.generation

import java.util.*
import kotlin.random.Random.Default.nextInt
import kotlinmud.world.BiomeType
import kotlinmud.world.room.RegenLevel
import kotlinmud.world.room.Room

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
            return BiomeType.values().indexOf(biomeType) + 1
        }
    }

    fun generate(): World {
        var id = 0
        val rooms = mutableListOf<Room>()
        return World(rooms, Array(DEPTH) {
            Array(length) {
                IntArray(width) {
                    rooms.add(Room(
                        id,
                        "area",
                        "name",
                        "description",
                        RegenLevel.NORMAL,
                        true,
                        BiomeType.ARBOREAL,
                        mutableListOf(),
                        mutableListOf(),
                        null
                    ))
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
            biomeLayer[w][l] = biome
            coordinates.add(Pair(w, l))
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
                    if (biomeLayer[w][l] == 0) {
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
        for (w in c1.first - 1..c2.first + 1) {
            for (l in c1.second - 1..c2.second + 1) {
                if (isInBounds(l, w) && ((w <= c1.first || w >= c2.first) || (l <= c1.second || l >= c2.second)) && biomeLayer[w][l] == 0) {
//                    println("filling w: $w, l: $l, value: $value")
                    biomeLayer[w][l] = value
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
