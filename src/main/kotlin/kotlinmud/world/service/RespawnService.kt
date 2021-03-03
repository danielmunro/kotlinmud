package kotlinmud.world.service

import kotlinmud.item.model.ItemRespawn
import kotlinmud.item.service.ItemRespawnService
import kotlinmud.mob.model.MobRespawn
import kotlinmud.mob.service.MobRespawnService
import kotlinmud.mob.service.MobService
import kotlinmud.room.service.RoomService

class RespawnService(
        private val mobService: MobService,
        private val roomService: RoomService,
        private val itemRespawns: List<ItemRespawn>,
        private val mobRespawns: List<MobRespawn>
) {
    fun respawn() {
        ItemRespawnService(itemRespawns).respawn()
        MobRespawnService(mobRespawns, mobService, roomService).respawn()
    }
}
