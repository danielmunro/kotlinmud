package kotlinmud.persistence.service

import kotlinmud.helper.logger
import kotlinmud.item.service.ItemService
import kotlinmud.mob.service.MobService
import kotlinmud.persistence.exception.RoomConnectionException
import kotlinmud.persistence.model.DoorModel
import kotlinmud.persistence.model.FileModel
import kotlinmud.persistence.model.ItemMobRespawnModel
import kotlinmud.persistence.model.ItemModel
import kotlinmud.persistence.model.MobModel
import kotlinmud.persistence.model.QuestModel
import kotlinmud.persistence.model.RoomModel
import kotlinmud.persistence.parser.ParserService
import kotlinmud.persistence.validator.ModelCollectionValidator
import kotlinmud.quest.service.QuestBuilderService
import kotlinmud.room.builder.RoomBuilder
import kotlinmud.room.model.Area
import kotlinmud.room.model.Door
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Direction
import kotlinmud.room.type.DoorDisposition

class StartupService(
    private val roomService: RoomService,
    private val mobService: MobService,
    private val itemService: ItemService,
    private val data: List<String>,
) {
    private val roomMap = mutableMapOf<Int, Room>()
    private val rooms = mutableListOf<RoomModel>()
    private val doors = mutableListOf<DoorModel>()
    private val mobs = mutableListOf<MobModel>()
    private val items = mutableListOf<ItemModel>()
    private val quests = mutableListOf<QuestModel>()
    private val itemMobRespawns = mutableListOf<ItemMobRespawnModel>()
    private val areas = mutableListOf<Area>()
    private val logger = logger(this)

    fun hydrateWorld() {
        data.map { section ->
            ParserService(section).parse().also {
                logger.debug("combine models for area into collection -- {}", it.area.name)
                combineModels(it)
                generateRoomsFromModels(it)
            }
        }.forEach {
            generateQuestsFromModels(it)
        }

        ModelCollectionValidator(
            rooms,
            mobs,
            items,
            quests,
        ).validate()

        logger.debug("--- model parse complete ---")
        logger.debug("parse stats -- ${rooms.size} rooms, ${mobs.size} mobs, ${items.size} items, ${quests.size} quests")

        mobs.forEach { mobService.addModel(it) }
        rooms.forEach { roomService.addModel(it) }

        connectUpRooms()
        logger.debug("rooms connected")

        createDoors()
        logger.debug("create doors")

        respawn()
        logger.debug("world respawned")
    }

    fun respawn() {
        createRespawnService().also {
            it.respawn()
        }
    }

    private fun createRespawnService(): RespawnService {
        return RespawnService(
            mobs,
            items,
            roomService,
            itemService,
            mobService,
        )
    }

    private fun createDoors() {
        doors.forEach {
            val disposition = DoorDisposition.valueOf(it.keywords.getOrDefault("disposition", "closed").toUpperCase())
            val keyId = it.keywords.getOrDefault("key", "0").toInt()
            val door = Door(
                it.name,
                it.brief,
                it.description,
                disposition,
                keyId,
            )
            val direction = Direction.valueOf(
                it.keywords.getOrElse("direction") {
                    throw Exception()
                }.toUpperCase()
            )
            val roomId = it.keywords.getOrElse("room") {
                throw Exception()
            }.toInt()
            val room = roomMap[roomId]!!
            when (direction) {
                Direction.NORTH -> {
                    room.northDoor = door
                    room.north!!.southDoor = door
                }
                Direction.SOUTH -> {
                    room.southDoor = door
                    room.south!!.northDoor = door
                }
                Direction.WEST -> {
                    room.westDoor = door
                    room.west!!.eastDoor = door
                }
                Direction.EAST -> {
                    room.eastDoor = door
                    room.east!!.westDoor = door
                }
                Direction.UP -> {
                    room.upDoor = door
                    room.up!!.downDoor = door
                }
                Direction.DOWN -> {
                    room.downDoor = door
                    room.down!!.upDoor = door
                }
            }
        }
    }

    private fun connectUpRooms() {
        val connect = { startId: Int, endId: Int, direction: String ->
            val start = roomMap[startId]
            val end = roomMap[endId]
            if (start == null) {
                throw RoomConnectionException(startId)
            }
            if (end == null) {
                throw RoomConnectionException(endId)
            }
            when (direction) {
                Direction.NORTH.value -> start.north = end
                Direction.SOUTH.value -> start.south = end
                Direction.EAST.value -> start.east = end
                Direction.WEST.value -> start.west = end
                Direction.UP.value -> start.up = end
                Direction.DOWN.value -> start.down = end
            }
        }
        rooms.forEach { model ->
            model.keywords.forEach {
                val keyword = it.key
                val directions = Direction.values().map { direction -> direction.value }
                if (directions.contains(keyword) && it.value.toInt() > 0) {
                    connect(model.id, it.value.toInt(), keyword)
                }
            }
        }
    }

    private fun combineModels(file: FileModel) {
        val area = file.area
        rooms.addAll(file.rooms)
        doors.addAll(file.doors)
        quests.addAll(file.quests)
        itemMobRespawns.addAll(file.itemMobRespawns)
        mobs.addAll(file.mobs)
        items.addAll(file.items)
        areas.add(area)
    }

    private fun generateRoomsFromModels(file: FileModel) {
        logger.debug("generating room models for area {}", file.area.name)
        val area = file.area
        file.rooms.forEach {
            createRoomFromModel(it, area)
        }
    }

    private fun generateQuestsFromModels(file: FileModel) {
        file.quests.forEach {
            createQuestFromModel(it)
        }
    }

    private fun createRoomFromModel(model: RoomModel, area: Area) {
        logger.debug("add room to area -- {}, {}", area, model.id)
        RoomBuilder(roomService).also {
            it.area = area
            it.id = model.id
            it.name = model.name
            it.description = model.description
            model.keywords.forEach { k ->
                it.setFromKeyword(k.key, k.value)
            }
        }.build().also {
            roomMap[model.id] = it
        }
    }

    private fun createQuestFromModel(model: QuestModel) {
        logger.debug("quest -- {}, {}", model.id, model.name)
        QuestBuilderService(
            mobService,
            roomService,
        ).also { builder ->
            builder.id = model.id
            builder.name = model.name
            builder.brief = model.brief
            builder.description = model.description
            model.keywords.forEach {
                builder.setFromKeyword(it.key, it.value)
            }
        }.build()
    }
}
