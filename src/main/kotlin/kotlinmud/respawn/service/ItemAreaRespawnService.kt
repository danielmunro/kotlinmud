package kotlinmud.respawn.service

import kotlinmud.helper.logger
import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.service.ItemService
import kotlinmud.respawn.model.ItemAreaRespawn
import kotlinmud.respawn.type.RespawnSomethingService
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import java.util.UUID

class ItemAreaRespawnService(
    private val roomService: RoomService,
    private val itemService: ItemService,
    private val respawns: List<ItemAreaRespawn>,
) : RespawnSomethingService {
    private val logger = logger(this)

    override suspend fun respawn() {
        respawns.forEach {
            doRespawn(
                it.area,
                it.maxAmount,
                it.canonicalId,
                it.itemBuilder,
            )
        }
    }

    private fun doRespawn(area: Area, maxAmount: Int, canonicalId: UUID, itemBuilder: ItemBuilder) {
        val rooms = roomService.findByArea(area)
        val count = itemService.findByCanonicalId(canonicalId).count()
        var amountToRespawn = Math.min(maxAmount - count, maxAmount)
        val randomSubset = rooms.filter { Math.random() < 0.3 }
        var i = 0

        // ensure the item inherits its own canonical ID
        itemBuilder.canonicalId = canonicalId

        if (amountToRespawn > 0) {
            logger.info("respawn ${itemBuilder.name} to $area (x$amountToRespawn)")
        }

        while (amountToRespawn > 0 && i < randomSubset.size) {
            itemBuilder.room = randomSubset[i]
            itemBuilder.build()
            amountToRespawn -= 1
            i += 1
        }
    }
}
