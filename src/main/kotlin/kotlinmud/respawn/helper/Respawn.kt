package kotlinmud.respawn.helper

import kotlinmud.item.model.ItemAreaRespawn
import kotlinmud.item.model.ItemMobRespawn
import kotlinmud.mob.model.MobRespawn

val itemMobRespawns = mutableListOf<ItemMobRespawn>()
val itemAreaRespawns = mutableListOf<ItemAreaRespawn>()
val mobRespawns = mutableListOf<MobRespawn>()

fun respawn(respawn: Any) {
    when (respawn) {
        is ItemMobRespawn -> itemMobRespawns.add(respawn)
        is ItemAreaRespawn -> itemAreaRespawns.add(respawn)
        is MobRespawn -> mobRespawns.add(respawn)
    }
}
