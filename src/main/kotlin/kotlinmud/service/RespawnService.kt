package kotlinmud.service

import kotlin.system.measureTimeMillis
import kotlinmud.fs.loader.area.model.reset.ItemMobReset
import kotlinmud.fs.loader.area.model.reset.ItemRoomReset
import kotlinmud.fs.loader.area.model.reset.MobReset
import kotlinmud.helper.logger
import kotlinmud.item.ItemService
import kotlinmud.item.model.ItemOwner
import kotlinmud.mob.MobService
import kotlinmud.mob.model.Mob
import kotlinmud.mob.model.MobBuilder
import kotlinmud.room.model.Room
import kotlinmud.world.World

class RespawnService(
    private val world: World,
    private val mobService: MobService,
    private val itemService: ItemService
) {
    private val logger = logger(this)
    private var mobIdAutoIncrementer = 0

    fun respawn() {
        logger.debug("respawn started")
        val time = measureTimeMillis {
            respawnFromResets()
            resetDoors()
        }
        logger.info("respawn occurred :: {} ms elapsed", time)
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
                    val origin = world.mobs.get(reset.mobId)
                    val mob = copy(origin)
                    items += addItemsToMob(mob)
                    mobService.putMobInRoom(mob, room)
                    mobs += 1
                }
            } catch (e: Exception) {
                logger.error("wiring problem with reset: $reset")
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
        logger.info("respawn stats :: $mobs mobs, $items items spawned")
    }

    private fun copy(mob: Mob): Mob {
        return MobBuilder(mob)
            .gold((mob.goldMin..mob.goldMax).random())
            .build()
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
        return reset.maxInInventory > itemService.findAllByOwner(room).filter { it.id == reset.itemId }.size &&
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
