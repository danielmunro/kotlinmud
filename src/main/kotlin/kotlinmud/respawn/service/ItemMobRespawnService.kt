package kotlinmud.respawn.service

import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.model.ItemMobRespawn
import kotlinmud.item.type.ItemCanonicalId
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.MobCanonicalId
import kotlinmud.respawn.type.RespawnSomethingService

class ItemMobRespawnService(
    private val mobService: MobService,
    private val respawns: List<ItemMobRespawn>,
) : RespawnSomethingService {
    override suspend fun respawn() {
        println("item mob respawns with ${respawns.size} respawns")
        respawns.forEach {
            doRespawn(it.mobCanonicalId, it.canonicalId, it.itemBuilder, it.maxAmount)
        }
    }

    private fun doRespawn(mobCanonicalId: MobCanonicalId, itemCanonicalId: ItemCanonicalId, itemBuilder: ItemBuilder, maxAmount: Int) {
        val mobs = mobService.findMobsByCanonicalId(mobCanonicalId)
        println("found ${mobs.size} mobs")
        mobs.forEach { mob ->
            println("found $mob for item $itemCanonicalId respawn")
            val count = mob.items.filter { it.canonicalId == itemCanonicalId }.size
            val difference = maxAmount - count
            if (difference > 0) {
                val toAdd = List(difference) { itemBuilder.build() }
                mob.items.addAll(toAdd)
            }
        }
    }
}
