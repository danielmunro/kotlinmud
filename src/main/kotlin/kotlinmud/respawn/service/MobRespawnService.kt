package kotlinmud.respawn.service

import kotlinmud.attributes.type.Attribute
import kotlinmud.mob.builder.MobBuilder
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.MobCanonicalId
import kotlinmud.respawn.helper.calculateHpForMob
import kotlinmud.respawn.model.MobRespawn
import kotlinmud.respawn.type.RespawnSomethingService
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area

class MobRespawnService(
    private val mobService: MobService,
    private val roomService: RoomService,
    private val respawns: List<MobRespawn>,
) : RespawnSomethingService {
    override suspend fun respawn() {
        respawns.forEach {
            doRespawn(
                it.area,
                it.maxAmount,
                it.mobBuilder.canonicalId!!,
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

        // ensure the mob inherits its own canonical ID
        mobBuilder.also { it.canonicalId = canonicalId }

        if (amountToRespawn > 0) {
            println("respawn ${mobBuilder.name} to $area (x$amountToRespawn)")
        }

        while (amountToRespawn > 0 && i < randomSubset.size) {
            val hp = calculateHpForMob(
                mobBuilder.level,
                mobBuilder.race,
            )
            mobBuilder.also {
                it.room = randomSubset[i]
                it.hp = hp
                it.attributes[Attribute.HP] = hp
            }.build()
            amountToRespawn -= 1
            i += 1
        }
    }
}
