package kotlinmud.saver.mapper.reset

import kotlinmud.loader.model.reset.ItemRoomReset

fun mapItemRoomReset(itemRoomReset: ItemRoomReset): String {
    return "itemId: ${itemRoomReset.itemId}, roomId: ${itemRoomReset.roomId}, maxInInventory: ${itemRoomReset.maxInInventory}, maxInWorld: ${itemRoomReset.maxInWorld}~"
}
