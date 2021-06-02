package kotlinmud.respawn.service

import kotlinmud.attributes.type.Attribute
import kotlinmud.helper.logger
import kotlinmud.mob.builder.MobBuilder
import kotlinmud.mob.service.MobService
import kotlinmud.respawn.helper.calculateHpForMob
import kotlinmud.respawn.model.MobRespawn
import kotlinmud.respawn.type.RespawnSomethingService
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import java.util.UUID

class MobRespawnService(
    private val mobService: MobService,
    private val roomService: RoomService,
    private val respawns: List<MobRespawn>,
) : RespawnSomethingService {
    private val logger = logger(this)

    override suspend fun respawn() {
        respawns.forEach {
            doRespawn(
                it.area,
                it.maxAmount,
                it.mobBuilder.canonicalId,
                it.mobBuilder,
            )
        }
    }

    private fun doRespawn(area: Area, maxAmount: Int, canonicalId: UUID, mobBuilder: MobBuilder) {
        val rooms = roomService.findByArea(area)
        val count = mobService.findMobsByCanonicalId(canonicalId).size
        var amountToRespawn = Math.min(maxAmount - count, maxAmount)
        var i = 0

        // ensure the mob inherits its own canonical ID
        mobBuilder.also { it.canonicalId = canonicalId }

        if (amountToRespawn > 0) {
            logger.info("respawn ${mobBuilder.name} to $area (x$amountToRespawn)")
        }

        while (amountToRespawn > 0) {
            val hp = calculateHpForMob(
                mobBuilder.level,
                mobBuilder.race,
            )
            mobBuilder.also {
                if (it.randomizeRoom) {
                    it.room = rooms.random()
                }
                it.hp = hp
                it.attributes[Attribute.HP] = hp
            }.build()
            amountToRespawn -= 1
            i += 1
        }
    }
}
