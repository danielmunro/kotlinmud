package kotlinmud.startup.service

import kotlinmud.attributes.type.Attribute
import kotlinmud.helper.logger
import kotlinmud.item.service.ItemService
import kotlinmud.mob.race.factory.createRaceFromString
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.service.MobService
import kotlinmud.respawn.helper.calculateHpForMob
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.startup.model.ItemModel
import kotlinmud.startup.model.ItemRoomRespawnModel
import kotlinmud.startup.model.MobModel
import kotlinmud.startup.model.MobRespawnModel
import kotlinmud.type.Builder

class RespawnService(
    private val mobs: List<MobModel>,
    private val items: List<ItemModel>,
    private val mobRespawns: List<MobRespawnModel>,
    private val itemRoomRespawns: List<ItemRoomRespawnModel>,
    private val roomService: RoomService,
    private val itemService: ItemService,
    private val mobService: MobService,
) {
    private val logger = logger(this)

    fun respawn() {
        respawnMobs()
        respawnItemsInRoom()
    }

    private fun respawnItemsInRoom() {
        val itemMap = mutableMapOf<Int, ItemModel>()
        items.forEach {
            itemMap[it.id] = it
        }
        itemRoomRespawns.forEach {
            val item = itemMap[it.itemId]!!
            val count = itemService.findById(it.itemId).size
            var amountToRespawn = Math.min(it.maxAmountInGame - count, it.maxAmountInGame)

            val builder = itemService.builder(
                item.name,
                item.description,
            )

            builder.brief = item.brief
            builder.id = item.id

            item.keywords.forEach { k -> builder.setFromKeyword(k.key, k.value) }

            if (amountToRespawn > 0) {
                logger.info("respawn ${builder.name} (x$amountToRespawn) to ${it.area}")
            }

            respawnBuilder(builder, Area.valueOf(it.area.name), it.roomId, amountToRespawn)
        }
    }

    private fun respawnMobs() {
        val mobMap = mutableMapOf<Int, MobModel>()
        mobs.forEach {
            mobMap[it.id] = it
        }
        mobRespawns.forEach {
            val mob = mobMap[it.mobId]!!
            val count = mobService.findMobsById(it.mobId).size
            val amountToRespawn = Math.min(it.maxAmountInGame - count, it.maxAmountInGame)

            val builder = mobService.builder(
                mob.name,
                mob.brief,
                mob.description,
            )

            builder.id = mob.id

            if (amountToRespawn > 0) {
                logger.info("respawn ${builder.name} (x$amountToRespawn) to ${it.area}")
            }

            val hp = calculateHpForMob(
                mob.keywords.getOrDefault("level", "1").toInt(),
                createRaceFromString(
                    RaceType.valueOf(
                        mob.keywords.getOrDefault("race", "human").toUpperCase()
                    )
                ),
            )
            builder.hp = hp
            builder.attributes[Attribute.HP] = hp
            mob.keywords.forEach { k -> builder.setFromKeyword(k.key, k.value) }
            respawnBuilder(builder, Area.valueOf(it.area.name), it.roomId, amountToRespawn)
        }
    }

    private fun respawnBuilder(builder: Builder, area: Area, roomId: Int, amountToRespawn: Int) {
        var decrementer = amountToRespawn
        val areaRooms = roomService.findByArea(area)
        while (decrementer > 0) {
            builder.room = if (roomId > 0) {
                roomService.findOne { room -> room.id == roomId }!!
            } else {
                areaRooms.random()
            }
            builder.build()
            decrementer--
        }
    }
}
