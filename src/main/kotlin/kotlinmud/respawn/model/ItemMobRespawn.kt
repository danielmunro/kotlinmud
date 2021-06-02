package kotlinmud.respawn.model

import kotlinmud.item.builder.ItemBuilder
import kotlinmud.respawn.type.Respawn
import java.util.UUID

class ItemMobRespawn(
    val itemBuilder: ItemBuilder,
    val mobCanonicalId: UUID,
    val maxAmount: Int = 1,
) : Respawn
