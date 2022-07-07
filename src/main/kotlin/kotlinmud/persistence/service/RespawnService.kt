package kotlinmud.persistence.service

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
import kotlinmud.persistence.model.ItemModel
import kotlinmud.persistence.model.MobModel
import kotlinmud.persistence.model.builder.RespawnSpec
import kotlinmud.persistence.model.builder.RespawnType
import kotlinmud.persistence.validator.ItemValidator
import kotlinmud.respawn.helper.calculateHpForMob
import kotlinmud.room.model.Area
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.type.Builder
import kotlin.system.measureTimeMillis

class RespawnService(
    private val mobs: List<MobModel>,
    private val items: List<ItemModel>,
    private val roomService: RoomService,
    private val itemService: ItemService,
    private val mobService: MobService,
) {
    private val logger = logger(this)

    fun respawn() {
        val timing = measureTimeMillis {
            respawnMobs()
            respawnItems()
        }
        logger.info("respawn took $timing ms")
    }

    private fun respawnItems() {
        items.forEach { item ->
            item.respawns.forEach {
                when (it.type) {
                    RespawnType.Room -> respawnItemsForRooms(item, it)
                    RespawnType.Mob -> respawnToMob(createItemBuilder(item), it)
                    RespawnType.Item -> return
                }
            }
        }
    }

    private fun respawnItemsForRooms(item: ItemModel, respawnSpec: RespawnSpec) {
        val count = itemService.findById(item.id).size
        val builder = createItemBuilder(item)
        respawnToRoom(
            builder,
            item.area,
            respawnSpec.targetId,
            respawnSpec.maxInGame - count,
            respawnSpec.maxAmount,
        ) { room ->
            room.items.filter { i -> i.id == item.id }.size
        }
    }

    private fun respawnMobs() {
        val mobMap = mutableMapOf<Int, MobModel>()
        mobs.forEach {
            mobMap[it.id] = it
        }
        mobs.forEach { mob ->
            val count = mobService.findMobsById(mob.id).size
            val builder = createMobBuilder(mob)
            mob.respawns.forEach { respawn ->
                respawnToRoom(
                    builder,
                    mob.area,
                    respawn.targetId,
                    respawn.maxInGame - count,
                    respawn.maxAmount,
                ) { room ->
                    mobService.findMobsInRoom(room).filter { mob.id == it.id }.size
                }
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

    private fun respawnToMob(builder: ItemBuilder, respawnSpec: RespawnSpec) {
        var amountToRespawn = respawnSpec.maxInGame
        val mobs = mobService.findMobsById(respawnSpec.targetId)
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

            val amountToCreate = respawnSpec.maxAmount - itemCount
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
