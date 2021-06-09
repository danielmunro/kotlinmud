package kotlinmud.world.service

import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.service.ItemService
import kotlinmud.mob.builder.MobBuilder
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.JobType
import kotlinmud.mob.type.QuestGiver
import kotlinmud.respawn.helper.itemRespawnsFor
import kotlinmud.room.builder.RoomBuilder
import kotlinmud.room.builder.build
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction

class AreaBuilderService(
    private val mobService: MobService,
    private val roomService: RoomService,
    private val itemService: ItemService,
    private val area: Area,
) {

    private lateinit var lastRoomBuilder: RoomBuilder
    private lateinit var lastRoom: Room
    private lateinit var lastLastRoom: Room

    fun createNewArea(area: Area): AreaBuilderService {
        return AreaBuilderService(
            mobService,
            roomService,
            itemService,
            area,
        )
    }

    fun startWith(room: Room): AreaBuilderService {
        lastRoom = room
        return this
    }

    fun startWith(label: String): AreaBuilderService {
        lastRoom = roomService.findOne { it.label == label }!!
        return this
    }

    fun connectRooms(source: String, dest: String, direction: Direction) {
        val sourceRoom = roomService.findOne { it.label == source }!!
        val destRoom = roomService.findOne { it.label == dest }!!
        connect(sourceRoom).toRoom(destRoom, direction)
    }

    fun roomBuilder(name: String, description: String): RoomBuilder {
        return roomService.builder(
            name,
            description,
            area,
        ).also {
            lastRoomBuilder = it
        }
    }

    fun connectTo(room: Room, direction: Direction): AreaBuilderService {
        connect(lastRoom).toRoom(room, direction)
        return this
    }

    fun connectTo(direction: Direction): AreaBuilderService {
        connect(lastLastRoom).toRoom(lastRoom, direction)
        return this
    }

    fun buildRoom(label: String, direction: Direction): AreaBuilderService {
        buildRoom(label)
        connectTo(direction)
        return this
    }

    fun buildRoom(direction: Direction, modifier: (RoomBuilder) -> Unit = {}): AreaBuilderService {
        lastLastRoom = lastRoom
        buildRoom(null, direction, modifier)
        return this
    }

    fun buildRoom(label: String? = null, direction: Direction? = null, modifier: (RoomBuilder) -> Unit = {}): AreaBuilderService {
        build(
            lastRoomBuilder.copy(modifier).also {
                lastRoomBuilder = it
                lastRoomBuilder.label = label
            }
        ).also {
            if (this::lastRoom.isInitialized) {
                lastLastRoom = lastRoom
            }
            lastRoom = it
        }
        if (direction != null) {
            connectTo(direction)
        }
        return this
    }

    fun getLastRoom(): Room {
        return lastRoom
    }

    fun itemBuilder(name: String, description: String, weight: Double = 1.0): ItemBuilder {
        return itemService.builder(name, description, weight)
    }

    fun roomItemBuilder(name: String, description: String, weight: Double = 1.0): ItemBuilder {
        return itemBuilder(name, description, weight).also {
            it.room = lastRoom
        }
    }

    fun buildShopkeeper(
        name: String,
        brief: String,
        description: String,
        race: Race,
        items: Map<ItemBuilder, Int>,
    ) {
        mobService.builder(
            name,
            brief,
            description,
            race
        ).also {
            it.room = lastRoom
            it.makeShopkeeper()
            itemRespawnsFor(it.canonicalId, items)
        }.build()
    }

    fun buildQuestGiver(
        name: String,
        brief: String,
        description: String,
        race: Race,
        identifier: QuestGiver
    ) {
        mobService.builder(
            name,
            brief,
            description,
            race,
        ).also {
            it.room = lastRoom
            it.job = JobType.QUEST
            it.identifier = identifier
        }.build()
    }

    fun buildFodder(
        name: String,
        brief: String,
        description: String,
        race: Race,
        level: Int,
        maxAmount: Int,
    ): MobBuilder {
        return mobService.buildFodder(
            name,
            brief,
            description,
            race,
            level,
            area,
            maxAmount,
        )
    }

    fun buildScavenger(
        name: String,
        brief: String,
        description: String,
        race: Race,
        level: Int,
        maxAmount: Int,
    ): MobBuilder {
        return mobService.buildFodder(
            name,
            brief,
            description,
            race,
            level,
            area,
            maxAmount,
        ).also {
            it.job = JobType.SCAVENGER
        }
    }
}
