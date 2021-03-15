package kotlinmud.item.model

import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.type.ItemCanonicalId
import kotlinmud.mob.type.MobCanonicalId

class ItemMobRespawn(
    val canonicalId: ItemCanonicalId,
    val itemBuilder: ItemBuilder,
    val mobCanonicalId: MobCanonicalId,
    val maxAmount: Int = 1,
)
