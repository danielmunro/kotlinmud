package kotlinmud.item.service

import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.model.ItemRespawn
import kotlinmud.item.type.ItemCanonicalId
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area

class ItemRespawnService(
    private val roomService: RoomService,
    private val itemService: ItemService,
    private val respawns: List<ItemRespawn>
) {
    fun respawn() {
        respawns.forEach {
            doRespawn(
                it.area,
                it.maxAmount,
                it.canonicalId,
                it.itemBuilder,
            )
        }
    }

    private fun doRespawn(area: Area, maxAmount: Int, canonicalId: ItemCanonicalId, itemBuilder: ItemBuilder) {
        val rooms = roomService.findByArea(area)
        val count = itemService.findByCanonicalId(canonicalId).count()
        var amountToRespawn = Math.min(maxAmount - count, maxAmount)
        val randomSubset = rooms.filter { Math.random() < 0.3 }
        var i = 0

        // ensure the item inherits its own canonical ID
        itemBuilder.canonicalId(canonicalId)

        while (amountToRespawn > 0 && i < randomSubset.size) {
            itemBuilder.room(randomSubset[i]).build()
            amountToRespawn -= 1
            i += 1
        }
    }
}
