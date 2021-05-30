package kotlinmud.world

import kotlinmud.item.service.ItemService
import kotlinmud.mob.service.MobService
import kotlinmud.room.service.RoomService
import kotlinmud.world.impl.itrias.lorimir.createLorimirForest
import kotlinmud.world.impl.itrias.lorimir.createLorimirForestLake
import kotlinmud.world.impl.itrias.lorimir.createLorimirForestOutpost
import kotlinmud.world.impl.itrias.troy.createTroyOutskirts
import kotlinmud.world.impl.itrias.troy.createTroyPromenade
import kotlinmud.world.impl.itrias.troy.createTroyTownCenter

fun createWorld(mobService: MobService, itemService: ItemService, roomService: RoomService) {
    val lorimirOutpost = createLorimirForestOutpost(mobService, itemService, roomService)
    val lorimirForest = createLorimirForest(mobService, roomService, itemService, lorimirOutpost)
    val outskirtsConnection = createTroyOutskirts(mobService, roomService, lorimirOutpost)
    val promenade = createTroyPromenade(roomService, mobService, outskirtsConnection)
    createTroyTownCenter(mobService, roomService, itemService, promenade)
    createLorimirForestLake(roomService, lorimirForest)
}
