package kotlinmud.fs.loader.model.reset

import kotlinmud.data.Row
import kotlinmud.fs.loader.model.Model

data class ItemRoomReset(override val id: Int, val itemId: Int, val roomId: Int, val maxInInventory: Int, val maxInWorld: Int) : Model,
    Row
