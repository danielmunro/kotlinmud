package kotlinmud.item.model

import kotlinmud.item.type.ItemCanonicalId

class Respawn(
    private val canonicalId: ItemCanonicalId,
    private val maxAmount: Int = 1,
)
