package kotlinmud.world.impl.itrias.troy

import kotlinmud.attributes.type.Attribute
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemCanonicalId
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.mob.race.impl.Undead
import kotlinmud.mob.service.MobService
import kotlinmud.respawn.helper.respawn
import kotlinmud.respawn.model.MobRespawn
import kotlinmud.room.builder.build
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Door
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction
import kotlinmud.room.type.DoorDisposition

fun createMudSchool(
    mobService: MobService,
    roomService: RoomService,
    itemService: ItemService,
    connection: Room,
) {
    val roomBuilder = roomService.builder(
        "The great entrance to the mud school",
        "tbd",
        Area.MudSchool,
    )

    val entrance = build(roomBuilder)
    val hallBuilder = roomBuilder.copy {
        it.name = "A magnificent hallway"
    }
    val hall1 = build(hallBuilder)
    val hall2 = build(hallBuilder)
    val hall3 = build(hallBuilder)
    val hall4 = build(hallBuilder)
    val hall5 = build(hallBuilder)

    connect(connection)
        .toRoom(entrance, Direction.WEST)
        .toRoom(hall1, Direction.WEST)
        .toRoom(hall2, Direction.WEST)
        .toRoom(hall3, Direction.DOWN)
        .toRoom(hall4, Direction.WEST)
        .toRoom(hall5, Direction.WEST)

    hall4.westDoor = Door(
        "a heavy wooden door",
        "tbd",
        DoorDisposition.LOCKED,
        ItemCanonicalId.MudSchoolKey,
    )

    val cageBuilder = roomBuilder.copy {
        it.name = "A large cage"
    }

    val sectionBuilder = {
        listOf(
            build(
                roomBuilder.copy {
                    it.name = "The center of a killing room"
                }
            ),
            build(cageBuilder),
            build(cageBuilder),
            build(cageBuilder),
        )
    }

    val weakMonsterBuilder = mobService.builder(
        "a weak monster",
        "a weak monster is here, chomping at your ankles",
        "tbd",
        Undead(),
    ).also {
        it.randomizeRoom = false
        it.attributes = mutableMapOf(
            Pair(Attribute.HIT, 4),
        )
    }

    val weakMobBuilderBuilder = { room: Room, item: Item ->
        respawn(
            MobRespawn(
                weakMonsterBuilder.copy().also {
                    it.room = room
                    it.equipped = listOf(item)
                },
                Area.MudSchool,
                1,
            )
        )
    }

    val hugeMonster = mobService.builder(
        "a huge monster",
        "a huge monster is here, mashing its teeth",
        "tbd",
    ).also {
        it.race = Undead()
        it.level = 2
        it.room = hall4
        it.randomizeRoom = false
        it.items = listOf(
            itemService.builder(
                "the mud school key",
                "tbd",
                0.1,
            ).also { item ->
                item.type = ItemType.KEY
                item.material = Material.COPPER
                item.canonicalId = ItemCanonicalId.MudSchoolKey
            }.build()
        )
    }

    respawn(MobRespawn(hugeMonster, Area.MudSchool, 1))

    //
    // First room area
    //

    val section1 = sectionBuilder()

    connect(hall1)
        .toRoom(section1[0], Direction.NORTH)
        .toRoom(
            listOf(
                Pair(section1[1], Direction.WEST),
                Pair(section1[2], Direction.NORTH),
                Pair(section1[3], Direction.EAST),
            )
        )

    weakMobBuilderBuilder(
        section1[1],
        itemService.builder(
            "a sub-issue cloak",
            "tbd",
            1.0,
        ).also { builder ->
            builder.type = ItemType.EQUIPMENT
            builder.position = Position.ABOUT
            builder.worth = 1
            builder.material = Material.TEXTILE
            builder.attributes = mapOf(
                Pair(Attribute.AC_SLASH, 1),
            )
        }.build()
    )

    weakMobBuilderBuilder(
        section1[2],
        itemService.builder(
            "a sub-issue helmet",
            "tbd",
            1.5,
        ).also { builder ->
            builder.type = ItemType.EQUIPMENT
            builder.position = Position.HEAD
            builder.worth = 1
            builder.material = Material.TEXTILE
            builder.attributes = mapOf(
                Pair(Attribute.AC_SLASH, 1),
                Pair(Attribute.AC_PIERCE, 1),
            )
        }.build()
    )

    weakMobBuilderBuilder(
        section1[3],
        itemService.builder(
            "a sub-issue tunic",
            "tbd",
            2.5,
        ).also { builder ->
            builder.type = ItemType.EQUIPMENT
            builder.position = Position.TORSO
            builder.worth = 1
            builder.material = Material.TEXTILE
            builder.attributes = mapOf(
                Pair(Attribute.AC_BASH, 2),
                Pair(Attribute.AC_SLASH, 2),
                Pair(Attribute.AC_PIERCE, 2),
            )
        }.build()
    )

    //
    // Second room area
    //

    val section2 = sectionBuilder()

    connect(hall1)
        .toRoom(section2[0], Direction.SOUTH)
        .toRoom(
            listOf(
                Pair(section2[1], Direction.WEST),
                Pair(section2[2], Direction.SOUTH),
                Pair(section2[3], Direction.EAST),
            )
        )

    weakMobBuilderBuilder(
        section2[1],
        itemService.builder(
            "sub-issue shoes",
            "tbd",
            3.0,
        ).also { builder ->
            builder.type = ItemType.EQUIPMENT
            builder.position = Position.FEET
            builder.worth = 1
            builder.material = Material.TEXTILE
            builder.attributes = mapOf(
                Pair(Attribute.AC_BASH, 1),
                Pair(Attribute.AC_SLASH, 1),
                Pair(Attribute.AC_PIERCE, 1),
            )
        }.build()
    )

    weakMobBuilderBuilder(
        section2[2],
        itemService.builder(
            "sub-issue arm guards",
            "tbd",
            2.5,
        ).also { builder ->
            builder.type = ItemType.EQUIPMENT
            builder.position = Position.ARMS
            builder.worth = 1
            builder.material = Material.TEXTILE
            builder.attributes = mapOf(
                Pair(Attribute.AC_BASH, 1),
                Pair(Attribute.AC_SLASH, 2),
                Pair(Attribute.AC_PIERCE, 2),
            )
        }.build()
    )

    weakMobBuilderBuilder(
        section2[3],
        itemService.builder(
            "sub-issue gloves",
            "tbd",
            1.0,
        ).also { builder ->
            builder.type = ItemType.EQUIPMENT
            builder.position = Position.HANDS
            builder.worth = 1
            builder.material = Material.TEXTILE
            builder.attributes = mapOf(
                Pair(Attribute.AC_BASH, 1),
                Pair(Attribute.AC_SLASH, 2),
                Pair(Attribute.AC_PIERCE, 2),
            )
        }.build()
    )

    //
    // Third room area
    //

    val section3 = sectionBuilder()

    connect(hall2)
        .toRoom(section3[0], Direction.NORTH)
        .toRoom(
            listOf(
                Pair(section3[1], Direction.WEST),
                Pair(section3[2], Direction.NORTH),
                Pair(section3[3], Direction.EAST),
            )
        )

    weakMobBuilderBuilder(
        section3[1],
        itemService.builder(
            "a sub-issue shield",
            "tbd",
            8.0,
        ).also { builder ->
            builder.type = ItemType.EQUIPMENT
            builder.position = Position.SHIELD
            builder.worth = 1
            builder.material = Material.WOOD
            builder.attributes = mapOf(
                Pair(Attribute.AC_BASH, 5),
                Pair(Attribute.AC_SLASH, 4),
                Pair(Attribute.AC_PIERCE, 4),
            )
        }.build()
    )

    weakMobBuilderBuilder(
        section3[2],
        itemService.builder(
            "sub-issue leggings",
            "tbd",
            2.5,
        ).also { builder ->
            builder.type = ItemType.EQUIPMENT
            builder.position = Position.LEGS
            builder.worth = 1
            builder.material = Material.TEXTILE
            builder.attributes = mapOf(
                Pair(Attribute.AC_BASH, 1),
                Pair(Attribute.AC_SLASH, 2),
                Pair(Attribute.AC_PIERCE, 2),
            )
        }.build()
    )

    weakMobBuilderBuilder(
        section3[3],
        itemService.builder(
            "a sub-issue belt",
            "tbd",
            2.0,
        ).also { builder ->
            builder.type = ItemType.EQUIPMENT
            builder.position = Position.WAIST
            builder.worth = 1
            builder.material = Material.TEXTILE
            builder.attributes = mapOf(
                Pair(Attribute.AC_BASH, 1),
                Pair(Attribute.AC_SLASH, 1),
                Pair(Attribute.AC_PIERCE, 1),
            )
        }.build()
    )

    //
    // Fourth room area
    //

    val section4 = sectionBuilder()

    connect(hall2)
        .toRoom(section4[0], Direction.SOUTH)
        .toRoom(
            listOf(
                Pair(section4[1], Direction.WEST),
                Pair(section4[2], Direction.SOUTH),
                Pair(section4[3], Direction.EAST),
            )
        )

    weakMobBuilderBuilder(
        section3[1],
        itemService.builder(
            "sub-issue bracers",
            "tbd",
            2.0,
        ).also { builder ->
            builder.type = ItemType.EQUIPMENT
            builder.position = Position.WRIST
            builder.worth = 1
            builder.material = Material.WOOD
            builder.attributes = mapOf(
                Pair(Attribute.AC_BASH, 1),
                Pair(Attribute.AC_SLASH, 2),
                Pair(Attribute.AC_PIERCE, 2),
            )
        }.build()
    )

    weakMobBuilderBuilder(
        section3[2],
        itemService.builder(
            "a sub-issue ring",
            "tbd",
            0.5,
        ).also { builder ->
            builder.type = ItemType.EQUIPMENT
            builder.position = Position.FINGER
            builder.worth = 1
            builder.material = Material.COPPER
            builder.attributes = mapOf(
                Pair(Attribute.AC_BASH, 0),
                Pair(Attribute.AC_SLASH, 1),
                Pair(Attribute.AC_PIERCE, 1),
            )
        }.build()
    )

    weakMobBuilderBuilder(
        section3[3],
        itemService.builder(
            "a floating stone",
            "tbd",
            1.0,
        ).also { builder ->
            builder.type = ItemType.EQUIPMENT
            builder.position = Position.FLOAT
            builder.worth = 1
            builder.material = Material.STONE
            builder.attributes = mapOf(
                Pair(Attribute.AC_MAGIC, 2),
            )
        }.build()
    )
}
