package kotlinmud.fs.saver.mapper.reset

import kotlinmud.fs.loader.model.reset.ItemMobReset

fun mapItemMobReset(itemMobReset: ItemMobReset): String {
    return "itemId: ${itemMobReset.itemId}, mobId: ${itemMobReset.mobId}, maxInInventory: ${itemMobReset.maxInInventory}, maxInWorld: ${itemMobReset.maxInWorld}~"
}
