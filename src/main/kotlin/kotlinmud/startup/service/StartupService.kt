package kotlinmud.startup.service

import kotlinmud.helper.logger
import kotlinmud.item.service.ItemService
import kotlinmud.mob.service.MobService
import kotlinmud.quest.service.QuestBuilderService
import kotlinmud.room.builder.RoomBuilder
import kotlinmud.room.model.Door
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction
import kotlinmud.room.type.DoorDisposition
import kotlinmud.startup.exception.RoomConnectionException
import kotlinmud.startup.model.DoorModel
import kotlinmud.startup.model.FileModel
import kotlinmud.startup.model.ItemMobRespawnModel
import kotlinmud.startup.model.ItemModel
import kotlinmud.startup.model.MobModel
import kotlinmud.startup.model.QuestModel
import kotlinmud.startup.model.RoomModel
import kotlinmud.startup.parser.ParserService
import kotlinmud.startup.validator.ModelCollectionValidator

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
                "n" -> start.north = end
                "s" -> start.south = end
                "e" -> start.east = end
                "w" -> start.west = end
                "u" -> start.up = end
                "d" -> start.down = end
            }
        }
        rooms.forEach { model ->
            model.keywords.forEach {
                val keyword = it.key
                if (keyword == "n" ||
                    keyword == "s" ||
                    keyword == "e" ||
                    keyword == "w" ||
                    keyword == "u" ||
                    keyword == "d"
                ) {
                    connect(model.id, it.value.toInt(), keyword)
                }
            }
        }
    }

    private fun combineModels(file: FileModel) {
        val area = Area.valueOf(file.area.name)
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
        val area = Area.valueOf(file.area.name)
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
        logger.debug("add room to area -- {}, {}", area.name, model.id)
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
