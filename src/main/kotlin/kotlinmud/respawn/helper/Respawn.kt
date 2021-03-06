package kotlinmud.respawn.helper

import kotlinmud.item.builder.ItemBuilder
import kotlinmud.mob.builder.MobBuilder
import kotlinmud.respawn.model.ItemAreaRespawn
import kotlinmud.respawn.model.ItemMobRespawn
import kotlinmud.respawn.model.MobRespawn
import kotlinmud.respawn.type.Respawn
import kotlinmud.room.type.Area
import java.util.UUID

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

fun itemRespawnsFor(mobCanonicalId: UUID, items: Map<ItemBuilder, Int>) {
    items.forEach {
        respawn(
            ItemMobRespawn(
                it.key, mobCanonicalId, it.value
            )
        )
    }
}

fun mobRespawnsFor(area: Area, mobs: List<Pair<MobBuilder, Int>>) {
    mobs.forEach {
        respawn(
            MobRespawn(
                it.first, area, it.second
            )
        )
    }
}
