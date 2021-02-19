package kotlinmud.mob.service

import kotlinmud.mob.helper.MobBuilder
import kotlinmud.mob.model.MobRespawn
import kotlinmud.mob.repository.countMobsByCanonicalId
import kotlinmud.mob.type.MobCanonicalId
import kotlinmud.room.repository.findRoomsByArea
import kotlinmud.room.type.Area

class MobRespawnService(private val respawns: List<MobRespawn>) {
    fun respawn() {
        respawns.forEach {
            doRespawn(
                it.area,
                it.maxAmount,
                it.canonicalId,
                it.mobBuilder,
            )
        }
    }

    private fun doRespawn(area: Area, maxAmount: Int, canonicalId: MobCanonicalId, mobBuilder: MobBuilder) {
        val rooms = findRoomsByArea(area)
        val count = countMobsByCanonicalId(canonicalId)
        var amountToRespawn = Math.min(maxAmount - count, maxAmount)
        val randomSubset = rooms.filter { Math.random() < 0.3 }
        var i = 0

        // ensure the item inherits its own canonical ID
        mobBuilder.canonicalId(canonicalId)

        while (amountToRespawn > 0 && i < randomSubset.size) {
            mobBuilder.room(randomSubset[i]).build()
            amountToRespawn -= 1
            i += 1
        }
    }
}
