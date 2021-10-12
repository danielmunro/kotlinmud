package kotlinmud.startup.service

import kotlinmud.attributes.type.Attribute
import kotlinmud.helper.logger
import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.Position
import kotlinmud.mob.builder.MobBuilder
import kotlinmud.mob.race.factory.createRaceFromString
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.service.MobService
import kotlinmud.respawn.helper.calculateHpForMob
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.startup.model.ItemMobRespawnModel
import kotlinmud.startup.model.ItemModel
import kotlinmud.startup.model.ItemRoomRespawnModel
import kotlinmud.startup.model.MobModel
import kotlinmud.startup.model.MobRespawnModel
import kotlinmud.startup.validator.ItemValidator
import kotlinmud.type.Builder

class RespawnService(
    private val mobs: List<MobModel>,
    private val items: List<ItemModel>,
    private val mobRespawns: List<MobRespawnModel>,
    private val itemRoomRespawns: List<ItemRoomRespawnModel>,
    private val itemMobRespawns: List<ItemMobRespawnModel>,
    private val roomService: RoomService,
    private val itemService: ItemService,
    private val mobService: MobService,
) {
    private val logger = logger(this)

    fun respawn() {
        respawnMobs()
        respawnItemsInRoom()
        respawnItemsForMobs()
    }

    private fun respawnItemsInRoom() {
        val itemMap = mutableMapOf<Int, ItemModel>()
        items.forEach {
            itemMap[it.id] = it
        }
        itemRoomRespawns.forEach {
            val item = itemMap[it.itemId]!!
//            val count = itemService.findById(it.itemId).size
//            val amountToRespawn = Math.min(it.maxAmountInGame - count, it.maxAmountInRoom)
//            logger.debug("item room respawn -- {}, {} existing, {} to create", item, count, amountToRespawn)
            val amountToRespawn = 1
            val builder = createItemBuilder(item)

            respawnToRoomBuilder(builder, Area.valueOf(it.area.name), it.roomId, amountToRespawn)
        }
    }

    private fun respawnItemsForMobs() {
        val itemMap = mutableMapOf<Int, ItemModel>()
        items.forEach {
            itemMap[it.id] = it
        }
        itemMobRespawns.forEach {
            val item = itemMap[it.itemId]!!
            val count = itemService.findById(it.itemId).size
            val amountToRespawn = Math.min(it.maxAmountInGame - count, it.maxAmountInGame)
            val builder = createItemBuilder(item)

            respawnToMobBuilder(builder, it.mobId, amountToRespawn, it.maxAmountForMob)
        }
    }

    private fun respawnMobs() {
        val mobMap = mutableMapOf<Int, MobModel>()
        mobs.forEach {
            mobMap[it.id] = it
        }
        mobRespawns.forEach {
            logger.debug("mob respawn -- {}, {}, {}", it.area.name, it.mobId, it.roomId)
            val mob = mobMap[it.mobId]!!
            val count = mobService.findMobsById(it.mobId).size
            val amountToRespawn = Math.min(it.maxAmountInGame - count, it.maxAmountInGame)
            val builder = createMobBuilder(mob)

            respawnToRoomBuilder(builder, Area.valueOf(it.area.name), it.roomId, amountToRespawn)
        }
    }

    private fun createItemBuilder(item: ItemModel): ItemBuilder {
        val builder = itemService.builder(
            item.name,
            item.description,
        )

        builder.brief = item.brief
        builder.id = item.id

        item.keywords.forEach { k -> builder.setFromKeyword(k.key, k.value) }
        return builder
    }

    private fun createMobBuilder(mob: MobModel): MobBuilder {
        val builder = mobService.builder(
            mob.name,
            mob.brief,
            mob.description,
        )

        builder.id = mob.id
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
        return builder
    }

    private fun respawnToRoomBuilder(builder: Builder, area: Area, roomId: Int, amountToRespawn: Int) {
        if (amountToRespawn > 0) {
            logger.info("respawn ${builder.name} (x$amountToRespawn) to $area")
        }
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

    private fun respawnToMobBuilder(builder: ItemBuilder, mobId: Int, amountToRespawn: Int, maxAmountForMob: Int) {
        if (amountToRespawn > 0) {
            logger.info("respawn ${builder.name} (x$amountToRespawn) to mob $mobId")
        }
        var decrementer = amountToRespawn
        val mobs = mobService.findMobsById(mobId)
        var i = 0
        while (decrementer > 0) {
            if (i >= mobs.size) {
                return
            }
            val mob = mobs[i]
            val itemCount = mob.items.count { it.id == builder.id }
            val amountToCreate = maxAmountForMob - itemCount
            for (j in 1..amountToCreate) {
                builder.build().also {
                    if (it.position == Position.NONE) {
                        mob.items.add(it)
                    } else {
                        mob.equipped.add(it)
                    }
                    ItemValidator(it).validate()
                }
            }
            decrementer -= amountToCreate
            i++
        }
    }
}