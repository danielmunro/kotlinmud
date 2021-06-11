package kotlinmud.world.service

import kotlinmud.helper.logger
import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.Material
import kotlinmud.item.type.Weapon
import kotlinmud.mob.builder.MobBuilder
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.JobType
import kotlinmud.mob.type.QuestGiver
import kotlinmud.respawn.helper.itemRespawnsFor
import kotlinmud.room.builder.RoomBuilder
import kotlinmud.room.builder.build
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Door
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
    lateinit var lastRoomBuilder: RoomBuilder
    lateinit var lastRoom: Room
    lateinit var lastLastRoom: Room
    private val logger = logger(this)

    fun copy(area: Area): AreaBuilderService {
        return AreaBuilderService(
            mobService,
            roomService,
            itemService,
            area,
        ).also {
            it.lastRoomBuilder = lastRoomBuilder
            it.lastRoom = lastRoom
            it.lastLastRoom = lastLastRoom
        }
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
        logger.info("{} is {} of {}", room.name, direction, lastRoom.name)
        connect(lastRoom).toRoom(room, direction)
        return this
    }

    fun connectTo(label: String, direction: Direction): AreaBuilderService {
        connect(lastRoom).toRoom(getRoomFromLabel(label), direction)
        return this
    }

    fun buildDoor(direction: Direction, door: Door): AreaBuilderService {
        when (direction) {
            Direction.WEST -> lastRoom.westDoor = door
            Direction.EAST -> lastRoom.eastDoor = door
            Direction.NORTH -> lastRoom.northDoor = door
            Direction.SOUTH -> lastRoom.southDoor = door
            Direction.UP -> lastRoom.upDoor = door
            Direction.DOWN -> lastRoom.downDoor = door
        }
        return this
    }

    fun getRoomFromLabel(label: String): Room {
        return roomService.findOne { it.label == label && it.area == area }!!
    }

    fun buildRoomFrom(fromLabel: String, toLabel: String, direction: Direction, modifier: (RoomBuilder) -> Unit = {}): AreaBuilderService {
        startWith(fromLabel)
        buildRoom(toLabel, direction, modifier)
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
        val roomBuilder = if (this::lastRoomBuilder.isInitialized)
            lastRoomBuilder
        else
            roomService.builder("", "", area).also {
                lastRoomBuilder = it
            }
        build(
            roomBuilder.copy(modifier).also {
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

    fun itemBuilder(name: String, description: String, weight: Double = 1.0): ItemBuilder {
        return itemService.builder(name, description, weight)
    }

    fun roomItemBuilder(name: String, description: String, weight: Double = 1.0): ItemBuilder {
        return itemBuilder(name, description, weight).also {
            it.room = lastRoom
        }
    }

    fun buildWeapon(
        name: String,
        description: String,
        weight: Double,
        type: Weapon,
        damageType: DamageType,
        material: Material,
        hit: Int,
        dam: Int,
        worth: Int,
        attackVerb: String = damageType.toString(),
    ): ItemBuilder {
        return itemService.buildWeapon(
            name,
            description,
            weight,
            type,
            damageType,
            material,
            hit,
            dam,
            worth,
            attackVerb,
        )
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

    private fun connectTo(direction: Direction): AreaBuilderService {
        connect(lastLastRoom).toRoom(lastRoom, direction)
        return this
    }
}
