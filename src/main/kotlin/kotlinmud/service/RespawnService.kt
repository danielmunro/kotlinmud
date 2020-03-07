package kotlinmud.service

import kotlinmud.loader.World
import kotlinmud.loader.model.reset.ItemMobReset
import kotlinmud.loader.model.reset.MobReset
import kotlinmud.mob.Mob
import kotlinmud.room.Room

class RespawnService(private val world: World, private val mobService: MobService) {
    fun respawn() {
        world.mobResets.toList().forEach { reset ->
            val room = world.rooms.get(reset.roomId)
            while (mobCanRespawn(reset, room)) {
                val mob = world.mobs.get(reset.mobId)
                addItemsToMob(mob)
                mobService.putMobInRoom(mob.copy(), room)
            }
        }
    }

    private fun addItemsToMob(mob: Mob) {
        filterItemMobResets(mob.id).forEach { itemReset ->
            val item = world.items.get(itemReset.itemId)
            mob.inventory.items.add(item.copy())
        }
    }

    private fun filterItemMobResets(mobId: Int): List<ItemMobReset> {
        return world.itemMobResets.toList().filter { it.mobId == mobId }
    }

    private fun mobCanRespawn(reset: MobReset, room: Room): Boolean {
        return reset.maxInRoom > mobService.getMobsForRoom(room).filter { mob -> mob.id == reset.mobId }.size &&
                reset.maxInWorld > mobService.getMobRooms().filter { mobRoom -> mobRoom.mob.id == reset.mobId }.size
    }
}
