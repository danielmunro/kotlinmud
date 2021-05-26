package kotlinmud.respawn.helper

import kotlinmud.item.builder.ItemBuilder
import kotlinmud.mob.type.MobCanonicalId
import kotlinmud.respawn.model.ItemAreaRespawn
import kotlinmud.respawn.model.ItemMobRespawn
import kotlinmud.respawn.model.MobRespawn
import kotlinmud.respawn.type.Respawn

val itemMobRespawns = mutableListOf<ItemMobRespawn>()
val itemAreaRespawns = mutableListOf<ItemAreaRespawn>()
val mobRespawns = mutableListOf<MobRespawn>()

fun respawn(respawn: Respawn) {
    when (respawn) {
        is ItemMobRespawn -> itemMobRespawns.add(respawn)
        is ItemAreaRespawn -> itemAreaRespawns.add(respawn)
        is MobRespawn -> mobRespawns.add(respawn)
    }
}

fun itemRespawnsFor(mobCanonicalId: MobCanonicalId, items: List<Pair<ItemBuilder, Int>>) {
    items.forEach {
        respawn(
            ItemMobRespawn(
                it.first, mobCanonicalId, it.second
            )
        )
    }
}
