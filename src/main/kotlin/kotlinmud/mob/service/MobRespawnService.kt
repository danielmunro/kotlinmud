package kotlinmud.mob.service

import kotlinmud.mob.builder.MobBuilder
import kotlinmud.mob.model.MobRespawn
import kotlinmud.mob.type.MobCanonicalId
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area

class MobRespawnService(
    private val respawns: List<MobRespawn>,
    private val mobService: MobService,
    private val roomService: RoomService
) {
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
        val rooms = roomService.findByArea(area)
        val count = mobService.findMobsByCanonicalId(canonicalId).size
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
