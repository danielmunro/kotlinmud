package kotlinmud.world

import kotlinmud.world.itrias.fusil.createFusilOutskirts
import kotlinmud.world.itrias.lorimir.createLorimirForest
import kotlinmud.world.itrias.lorimir.createLorimirForestLake
import kotlinmud.world.itrias.lorimir.createLorimirForestOutpost

fun createWorld() {
    val lorimirOutpost = createLorimirForestOutpost()
    val outskirtsConnection = createFusilOutskirts(lorimirOutpost)
    val lorimirForest = createLorimirForest(lorimirOutpost)
    createLorimirForestLake(lorimirForest)
}
