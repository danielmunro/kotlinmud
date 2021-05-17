package kotlinmud.respawn.model

import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.type.ItemCanonicalId
import kotlinmud.respawn.type.Respawn
import kotlinmud.room.type.Area

class ItemAreaRespawn(
    val canonicalId: ItemCanonicalId,
    val itemBuilder: ItemBuilder,
    val area: Area,
    val maxAmount: Int = 1,
) : Respawn
