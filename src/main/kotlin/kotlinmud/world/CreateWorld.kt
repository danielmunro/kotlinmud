package kotlinmud.world

import kotlinmud.item.service.ItemService
import kotlinmud.mob.service.MobService
import kotlinmud.room.service.RoomService
import kotlinmud.world.itrias.lorimir.createLorimirForest
import kotlinmud.world.itrias.lorimir.createLorimirForestLake
import kotlinmud.world.itrias.lorimir.createLorimirForestOutpost
import kotlinmud.world.itrias.troy.createTroyOutskirts
import kotlinmud.world.itrias.troy.createTroyPromenade
import kotlinmud.world.itrias.troy.createTroyTownCenter

fun createWorld(mobService: MobService, itemService: ItemService, roomService: RoomService) {
    val lorimirOutpost = createLorimirForestOutpost(mobService, itemService, roomService)
    val lorimirForest = createLorimirForest(mobService, roomService, itemService, lorimirOutpost)
    val outskirtsConnection = createTroyOutskirts(roomService, lorimirOutpost)
    val promenade = createTroyPromenade(roomService, outskirtsConnection)
    createTroyTownCenter(mobService, roomService, itemService, promenade)
    createLorimirForestLake(roomService, lorimirForest)
}
