package kotlinmud.respawn.model

import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.type.ItemCanonicalId
import kotlinmud.mob.type.MobCanonicalId
import kotlinmud.respawn.type.Respawn

class ItemMobRespawn(
    val canonicalId: ItemCanonicalId,
    val itemBuilder: ItemBuilder,
    val mobCanonicalId: MobCanonicalId,
    val maxAmount: Int = 1,
) : Respawn
