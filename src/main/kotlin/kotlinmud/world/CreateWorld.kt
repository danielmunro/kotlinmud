package kotlinmud.world

import kotlinmud.mob.service.MobService
import kotlinmud.world.itrias.fusil.createFusilOutskirts
import kotlinmud.world.itrias.lorimir.createLorimirForest
import kotlinmud.world.itrias.lorimir.createLorimirForestLake
import kotlinmud.world.itrias.lorimir.createLorimirForestOutpost

fun createWorld(mobService: MobService) {
    val lorimirOutpost = createLorimirForestOutpost(mobService)
    val outskirtsConnection = createFusilOutskirts(lorimirOutpost)
    val lorimirForest = createLorimirForest(mobService, lorimirOutpost)
    createLorimirForestLake(lorimirForest)
}
