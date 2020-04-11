package kotlinmud.fs.loader.model.reset

import kotlinmud.data.Row
import kotlinmud.fs.loader.model.Model

data class ItemMobReset(override val id: Int, val itemId: Int, val mobId: Int, val maxInInventory: Int, val maxInWorld: Int) : Model, Row
