package kotlinmud.item.service

import kotlinmud.item.helper.ItemBuilder
import kotlinmud.item.model.ItemRespawn
import kotlinmud.item.repository.countItemsByCanonicalId
import kotlinmud.item.type.ItemCanonicalId
import kotlinmud.room.repository.findRoomsByArea
import kotlinmud.room.type.Area

class ItemRespawnService(private val respawns: List<ItemRespawn>) {
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
        val rooms = findRoomsByArea(area)
        val count = countItemsByCanonicalId(canonicalId)
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