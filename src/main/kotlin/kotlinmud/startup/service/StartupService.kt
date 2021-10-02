package kotlinmud.startup.service

import kotlinmud.attributes.type.Attribute
import kotlinmud.helper.logger
import kotlinmud.mob.race.factory.createRaceFromString
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.service.MobService
import kotlinmud.respawn.helper.calculateHpForMob
import kotlinmud.room.builder.RoomBuilder
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.startup.exception.RoomConnectionException
import kotlinmud.startup.model.FileModel
import kotlinmud.startup.model.MobModel
import kotlinmud.startup.model.MobRespawnModel
import kotlinmud.startup.model.RoomModel
import kotlinmud.startup.parser.Parser
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class StartupService(private val roomService: RoomService, private val mobService: MobService) {
    private val roomMap = mutableMapOf<Int, Room>()
    private val rooms = mutableListOf<RoomModel>()
    private val mobs = mutableListOf<MobModel>()
    private val mobRespawns = mutableListOf<MobRespawnModel>()
    private val areas = mutableListOf<Area>()
    private val logger = logger(this)

    fun hydrateWorld() {
        println("hydrate world started")
        readWorldSourceFiles()
        connectUpRooms()
        respawnMobs()
        println("world hydration done -- ${rooms.size} rooms")
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
            var i = 0

            val builder = mobService.builder(
                mob.name,
                mob.brief,
                mob.description,
            )

            builder.id = mob.id

            if (amountToRespawn > 0) {
                logger.info("respawn ${builder.name} (x$amountToRespawn) to ${it.area}")
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
                builder.build()
                amountToRespawn -= 1
                i += 1
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
                if (keyword == "n" || keyword == "s" || keyword == "e" || keyword == "w" || keyword == "u" || keyword == "d") {
                    connect(model.id, it.value.toInt(), keyword)
                }
            }
        }
    }

    private fun readWorldSourceFiles() {
        Files.list(Paths.get("./world")).forEach {
            println(it)
            generateModels(Parser(File(it.toUri()).readText()).parse())
        }
    }

    private fun generateModels(file: FileModel) {
        val area = Area.valueOf(file.area.name)
        file.rooms.forEach { model ->
            val room = RoomBuilder(roomService).also {
                it.area = area
                it.id = model.id
                it.name = model.name
                it.description = model.description
            }.build()
            roomMap[model.id] = room
            rooms.add(model)
            roomService.add(room)
        }
        mobRespawns.addAll(file.mobRespawns)
        mobs.addAll(file.mobs)
        areas.add(area)
    }
}
