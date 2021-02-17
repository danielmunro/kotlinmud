package kotlinmud.world

import kotlinmud.world.itrias.lorimir.createLorimirForest
import kotlinmud.world.itrias.lorimir.createLorimirForestLake
import kotlinmud.world.itrias.lorimir.createLorimirForestOutpost

fun createWorld() {
    val connection1 = createLorimirForestOutpost()
    val connection2 = createLorimirForest(connection1)
    createLorimirForestLake(connection2)
}
