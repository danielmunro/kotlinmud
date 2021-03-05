package kotlinmud.world.service

import kotlinmud.item.model.ItemRespawn
import kotlinmud.item.service.ItemRespawnService
import kotlinmud.item.service.ItemService
import kotlinmud.mob.model.MobRespawn
import kotlinmud.mob.service.MobRespawnService
import kotlinmud.mob.service.MobService
import kotlinmud.room.service.RoomService

class RespawnService(
    private val mobService: MobService,
    private val roomService: RoomService,
    private val itemService: ItemService,
    private val itemRespawns: List<ItemRespawn>,
    private val mobRespawns: List<MobRespawn>
) {
    fun respawn() {
        ItemRespawnService(roomService, itemService, itemRespawns).respawn()
        MobRespawnService(mobRespawns, mobService, roomService).respawn()
    }
}
