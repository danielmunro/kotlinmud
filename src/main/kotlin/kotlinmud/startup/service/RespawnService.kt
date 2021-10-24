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
import kotlinmud.mob.type.JobType
import kotlinmud.respawn.helper.calculateHpForMob
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.startup.model.ItemMobRespawnModel
import kotlinmud.startup.model.ItemModel
import kotlinmud.startup.model.ItemRoomRespawnModel
import kotlinmud.startup.model.MobModel
import kotlinmud.startup.model.MobRespawnModel
import kotlinmud.startup.validator.ItemValidator
import kotlinmud.type.Builder
import kotlin.system.measureTimeMillis

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
        val timing = measureTimeMillis {
            respawnMobs()
            respawnItemsInRoom()
            respawnItemsForMobs()
        }
        logger.info("respawn took $timing ms")
    }

    private fun respawnItemsInRoom() {
        val itemMap = mutableMapOf<Int, ItemModel>()
        items.forEach {
            itemMap[it.id] = it
        }
        itemRoomRespawns.forEach {
            val item = itemMap[it.itemId]!!
            val count = itemService.findById(it.itemId).size
            val builder = createItemBuilder(item)

            respawnToRoom(
                builder,
                Area.valueOf(it.area.name),
                it.roomId,
                it.maxAmountInGame - count,
                it.maxAmountInRoom,
            ) { room ->
                room.items.filter { item -> item.id == it.itemId }.size
            }
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
            val amountToRespawn = it.maxAmountInGame - count
            val builder = createItemBuilder(item)

            respawnToMob(builder, it.mobId, amountToRespawn, it.maxAmountForMob)
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
            val builder = createMobBuilder(mob)

            respawnToRoom(
                builder,
                Area.valueOf(it.area.name),
                it.roomId,
                it.maxAmountInGame - count,
                it.maxAmountInRoom,
            ) { room ->
                mobService.findMobsInRoom(room).filter { mob -> mob.id == it.mobId }.size
            }
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

    private fun respawnToRoom(
        builder: Builder,
        area: Area,
        roomId: Int,
        maxAmountToRespawn: Int,
        maxAmountInRoom: Int,
        counter: (Room) -> Int,
    ) {
        var amountToRespawn = maxAmountToRespawn
        val areaRooms = roomService.findByArea(area)
        var amount = 0
        while (amountToRespawn > 0 && amount < maxAmountInRoom) {
            val room = if (roomId > 0) {
                roomService.findOne { room -> room.id == roomId }!!
            } else {
                areaRooms.random()
            }
            builder.room = room
            builder.build()
            logger.info("add ${builder.name} to $room")
            amountToRespawn--
            amount = counter(room)
        }
    }

    private fun respawnToMob(builder: ItemBuilder, mobId: Int, maxAmountInGame: Int, maxAmountForMob: Int) {
        var amountToRespawn = maxAmountInGame
        val mobs = mobService.findMobsById(mobId)
        var i = 0
        while (amountToRespawn > 0) {
            if (i >= mobs.size) {
                return
            }
            val mob = mobs[i]
            val whereToFind = builder.position == Position.NONE || mob.job == JobType.SHOPKEEPER
            val itemCount = if (whereToFind)
                mob.items.count { it.id == builder.id }
            else
                mob.equipped.count { it.id == builder.id }

            val amountToCreate = maxAmountForMob - itemCount
            for (j in 1..amountToCreate) {
                builder.build().also {
                    if (whereToFind) {
                        mob.items.add(it)
                    } else {
                        mob.equipped.add(it)
                    }
                    ItemValidator(it).validate()
                }
                logger.info("add ${builder.name} to mob $mob")
                amountToRespawn--
                if (amountToRespawn == 0) {
                    break
                }
            }
            i++
        }
    }
}
