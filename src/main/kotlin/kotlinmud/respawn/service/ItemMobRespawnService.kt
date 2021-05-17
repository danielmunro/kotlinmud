package kotlinmud.respawn.service

import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.type.ItemCanonicalId
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.MobCanonicalId
import kotlinmud.respawn.model.ItemMobRespawn
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
            val count = mob.items.stream().filter { it.canonicalId == itemCanonicalId }.count()
            val difference = (maxAmount - count).toInt()
            if (difference > 0) {
                val toAdd = List(difference) { itemBuilder.build() }
                mob.items.addAll(toAdd)
            }
        }
    }
}
