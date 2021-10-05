package kotlinmud.startup.service

import kotlinmud.attributes.type.Attribute
import kotlinmud.helper.logger
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.mob.race.factory.createRaceFromString
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.JobType
import kotlinmud.respawn.helper.calculateHpForMob
import kotlinmud.room.service.RoomService
import kotlinmud.startup.model.ItemModel
import kotlinmud.startup.model.ItemRoomRespawnModel
import kotlinmud.startup.model.MobModel
import kotlinmud.startup.model.MobRespawnModel

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
            val rooms = roomService.findByArea(it.area)
            val item = itemMap[it.itemId]!!
            val count = itemService.findById(it.itemId).size
            var amountToRespawn = Math.min(it.maxAmountInGame - count, it.maxAmountInGame)

            val builder = itemService.builder(
                item.name,
                item.description,
            )
            builder.brief = item.brief
            builder.id = item.id

            item.keywords.forEach { k ->
                val keyword = k.key
                val value = k.value
                when (keyword) {
                    "food" -> {
                        builder.type = ItemType.FOOD
                        builder.material = Material.ORGANIC
                        builder.quantity = value.toInt()
                    }
                    "weight" -> {
                        builder.weight = value.toDouble()
                    }
                    "type" -> {
                        builder.type = ItemType.valueOf(value.toUpperCase())
                    }
                    "position" -> {
                        builder.position = Position.valueOf(value.toUpperCase())
                    }
                    "material" -> {
                        builder.material = Material.valueOf(value.toUpperCase())
                    }
                }
            }

            if (amountToRespawn > 0) {
                logger.info("respawn ${builder.name} (x$amountToRespawn) to ${it.area}")
            }

            while (amountToRespawn > 0) {
                builder.room = rooms.random()
                builder.build()
                amountToRespawn--
            }
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
            var amountToRespawn = Math.min(it.maxAmountInGame - count, it.maxAmountInGame)

            val builder = mobService.builder(
                mob.name,
                mob.brief,
                mob.description,
            )

            builder.id = mob.id

            if (amountToRespawn > 0) {
                logger.info("respawn ${builder.name} (x$amountToRespawn) to ${it.area}")
            }

            if (builder.job == JobType.NONE) {
                builder.job = JobType.FODDER
            }

            while (amountToRespawn > 0) {
                val hp = calculateHpForMob(
                    mob.keywords.getOrDefault("level", "1").toInt(),
                    createRaceFromString(
                        RaceType.valueOf(
                            mob.keywords.getOrDefault("race", "human").toUpperCase()
                        )
                    ),
                )
                if (it.roomId > 0) {
                    builder.room = roomService.findOne { room -> room.id == it.roomId }!!
                } else {
                    builder.room = roomService.findByArea(it.area).random()
                }
                builder.hp = hp
                builder.attributes[Attribute.HP] = hp

                mob.keywords.forEach { k ->
                    val keyword = k.key
                    val value = k.value
                    when (keyword) {
                        "job" -> {
                            builder.job = JobType.valueOf(value.toUpperCase())
                        }
                    }
                }

                builder.build()
                amountToRespawn--
            }
        }
    }
}
