package kotlinmud.fs.saver.mapper.reset

import kotlinmud.fs.loader.model.reset.ItemRoomReset

fun mapItemRoomReset(itemRoomReset: ItemRoomReset): String {
    return "itemId: ${itemRoomReset.itemId}, roomId: ${itemRoomReset.roomId}, maxInInventory: ${itemRoomReset.maxInInventory}, maxInWorld: ${itemRoomReset.maxInWorld}~"
}
