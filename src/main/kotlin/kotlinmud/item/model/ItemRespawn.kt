package kotlinmud.item.model

import kotlinmud.item.helper.ItemBuilder
import kotlinmud.item.type.ItemCanonicalId
import kotlinmud.room.type.Area

class ItemRespawn(
    val canonicalId: ItemCanonicalId,
    val itemBuilder: ItemBuilder,
    val area: Area,
    val maxAmount: Int = 1,
)
