package kotlinmud.service

import kotlin.system.measureTimeMillis
import kotlinmud.item.ItemOwner
import kotlinmud.loader.World
import kotlinmud.loader.model.reset.ItemMobReset
import kotlinmud.loader.model.reset.ItemRoomReset
import kotlinmud.loader.model.reset.MobReset
import kotlinmud.mob.Mob
import kotlinmud.room.Room

class RespawnService(
    private val world: World,
    private val mobService: MobService,
    private val itemService: ItemService
) {

    fun respawn() {
        val time = measureTimeMillis {
            respawnFromResets()
            resetDoors()
        }
        println("respawn took $time ms.")
    }

    private fun resetDoors() {
        world.rooms.toList().forEach { room ->
            room.exits.filter { it.door != null }.forEach { it.door!!.reset() }
        }
    }

    private fun respawnFromResets() {
        var mobs = 0
        var items = 0
        world.mobResets.toList().forEach { reset ->
            try {
                val room = world.rooms.get(reset.roomId)
                while (mobCanRespawn(reset, room)) {
                    val mob = world.mobs.get(reset.mobId).copy()
                    items += addItemsToMob(mob)
                    mobService.putMobInRoom(mob, room)
                    mobs += 1
                }
            } catch (e: Exception) {
                println("wiring problem with reset: $reset")
                throw e
            }
        }
        world.itemRoomResets.toList().forEach { reset ->
            while (itemRoomCanRespawn(reset, world.rooms.get(reset.roomId))) {
                with(world.items.get(reset.itemId).copy()) {
                    val room = world.rooms.get(reset.roomId)
                    itemService.add(ItemOwner(this, room))
                }
                items += 1
            }
        }
        println("respawn complete :: $mobs mobs, $items items")
    }

    private fun addItemsToMob(mob: Mob): Int {
        var items = 0
        filterItemMobResets(mob.id).forEach { itemReset ->
            val item = world.items.get(itemReset.itemId)
            while (itemMobCanRespawn(itemReset, mob)) {
                with(item.copy()) {
                    itemService.add(ItemOwner(this, mob))
                }
                items++
            }
        }
        return items
    }

    private fun filterItemMobResets(mobId: Int): List<ItemMobReset> {
        return world.itemMobResets.toList().filter { it.mobId == mobId }
    }

    private fun itemRoomCanRespawn(reset: ItemRoomReset, room: Room): Boolean {
        val roomItems = itemService.findAllByOwner(room)
        return reset.maxInInventory > roomItems.filter { it.id == reset.itemId }.size &&
                reset.maxInWorld > itemService.countItemsById(reset.itemId)
    }

    private fun itemMobCanRespawn(reset: ItemMobReset, mob: Mob): Boolean {
        return reset.maxInInventory > itemService.findAllByOwner(mob).filter { it.id == reset.itemId }.size &&
                reset.maxInWorld > itemService.countItemsById(reset.itemId)
    }

    private fun mobCanRespawn(reset: MobReset, room: Room): Boolean {
        return reset.maxInRoom > mobService.getMobsForRoom(room).filter { mob -> mob.id == reset.mobId }.size &&
                reset.maxInWorld > mobService.getMobRooms().filter { mobRoom -> mobRoom.mob.id == reset.mobId }.size
    }
}
