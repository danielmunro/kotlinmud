package kotlinmud.world.service

import kotlinmud.item.model.ItemRespawn
import kotlinmud.item.service.ItemRespawnService
import kotlinmud.mob.model.MobRespawn
import kotlinmud.mob.service.MobRespawnService

class RespawnService(private val itemRespawns: List<ItemRespawn>, private val mobRespawns: List<MobRespawn>) {
    fun respawn() {
        ItemRespawnService(itemRespawns).respawn()
        MobRespawnService(mobRespawns).respawn()
    }
}