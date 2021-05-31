package kotlinmud.respawn.service

import kotlinmud.helper.logger
import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.type.ItemCanonicalId
import kotlinmud.mob.service.MobService
import kotlinmud.respawn.model.ItemMobRespawn
import kotlinmud.respawn.type.RespawnSomethingService
import java.util.*

class ItemMobRespawnService(
    private val mobService: MobService,
    private val respawns: List<ItemMobRespawn>,
) : RespawnSomethingService {
    private val logger = logger(this)

    override suspend fun respawn() {
        respawns.forEach {
            doRespawn(it.mobCanonicalId, it.itemBuilder.canonicalId!!, it.itemBuilder, it.maxAmount)
        }
    }

    private fun doRespawn(
        mobCanonicalId: UUID,
        itemCanonicalId: ItemCanonicalId,
        itemBuilder: ItemBuilder,
        maxAmount: Int,
    ) {
        mobService.findMobsByCanonicalId(mobCanonicalId).forEach { mob ->
            val count = mob.items.stream().filter { it.canonicalId == itemCanonicalId }.count()
            val difference = (maxAmount - count).toInt()
            if (difference > 0) {
                logger.info("found $mob for item $itemCanonicalId respawn")
                val toAdd = List(difference) { itemBuilder.build() }
                mob.items.addAll(toAdd)
            }
        }
    }
}
