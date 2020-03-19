package kotlinmud.loader.model.reset

import kotlinmud.data.Row
import kotlinmud.loader.model.Model

class ItemRoomReset(override val id: Int, val itemId: Int, val roomId: Int, val maxInInventory: Int, val maxInWorld: Int) : Model,
    Row
