package kotlinmud.respawn.model

import kotlinmud.item.builder.ItemBuilder
import kotlinmud.respawn.type.Respawn
import java.util.UUID

class ItemAreaRespawn(
    val canonicalId: UUID,
    val itemBuilder: ItemBuilder,
    val area: String,
    val maxAmount: Int = 1,
) : Respawn
