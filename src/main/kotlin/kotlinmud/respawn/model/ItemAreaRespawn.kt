package kotlinmud.respawn.model

import kotlinmud.item.builder.ItemBuilder
import kotlinmud.respawn.type.Respawn
import kotlinmud.room.type.Area
import java.util.UUID

class ItemAreaRespawn(
    val canonicalId: UUID,
    val itemBuilder: ItemBuilder,
    val area: Area,
    val maxAmount: Int = 1,
) : Respawn
