package kotlinmud.world

import kotlinmud.item.service.ItemService
import kotlinmud.mob.service.MobService
import kotlinmud.world.itrias.fusil.createFusilOutskirts
import kotlinmud.world.itrias.lorimir.createLorimirForest
import kotlinmud.world.itrias.lorimir.createLorimirForestLake
import kotlinmud.world.itrias.lorimir.createLorimirForestOutpost

fun createWorld(mobService: MobService, itemService: ItemService) {
    val lorimirOutpost = createLorimirForestOutpost(mobService, itemService)
    val outskirtsConnection = createFusilOutskirts(lorimirOutpost)
    val lorimirForest = createLorimirForest(mobService, lorimirOutpost)
    createLorimirForestLake(lorimirForest)
}
