package kotlinmud.world.impl.itrias.troy

import kotlinmud.attributes.type.Attribute
import kotlinmud.item.model.Item
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.race.impl.Undead
import kotlinmud.room.builder.build
import kotlinmud.room.model.Door
import kotlinmud.room.model.Room
import kotlinmud.room.type.Direction
import kotlinmud.room.type.DoorDisposition
import kotlinmud.world.service.AreaBuilderService
import java.util.UUID

fun createMudSchool(areaBuilderService: AreaBuilderService, connection: Room) {
    val keyId = UUID.randomUUID()
    val room4 = areaBuilderService.startWith(connection)
        .buildRoom(Direction.WEST) {
            it.name = "The great entrance to the mud school"
            it.description = "tbd"
        }
        .buildRoom("hall1", Direction.WEST) {
            it.name = "A magnificent hallway"
        }
        .buildRoom("hall2", Direction.WEST)
        .buildRoom("hall3", Direction.WEST)
        .buildRoom("hall4", Direction.WEST).lastRoom

    areaBuilderService
        .buildDoor(
            Direction.WEST,
            Door(
                "a heavy wooden door",
                "tbd",
                DoorDisposition.LOCKED,
                keyId,
            )
        )
        .buildRoom("hall5", Direction.WEST)

    areaBuilderService.buildFodder(
        "the mud school diploma guardian",
        "the mud school diploma guardian is here",
        "tbd",
        Human(),
        3,
        1,
    ).also {
        it.randomizeRoom = false
        it.room = areaBuilderService.lastRoom
        it.level = 3
        it.items = mutableListOf(
            areaBuilderService.itemBuilder(
                "mud school diploma",
                "tbd",
                0.1,
                20,
            ).also { item ->
                item.material = Material.PAPER
                item.type = ItemType.EQUIPMENT
                item.position = Position.HELD
                item.attributes = mapOf(
                    Pair(Attribute.WIS, 1),
                )
            }.build()
        )
    }

    val weakMobBuilder = { room: String, item: Item ->
        areaBuilderService
            .buildFodder(
                "a weak monster",
                "a weak monster is here, ready to attack",
                "tbd",
                Undead(),
                1,
                1,
            ).also {
                it.randomizeRoom = false
                it.room = areaBuilderService.getRoomFromLabel(room)
                it.equipped = mutableListOf(item)
            }
    }

    areaBuilderService.buildFodder(
        "a huge monster",
        "a huge monster is here, mashing its teeth",
        "tbd",
        Undead(),
        2,
        1,
    ).also {
        it.race = Undead()
        it.level = 2
        it.room = room4
        it.randomizeRoom = false
        it.items = mutableListOf(
            areaBuilderService.itemBuilder(
                "the mud school key",
                "tbd",
                0.1,
                0,
            ).also { item ->
                item.type = ItemType.KEY
                item.material = Material.COPPER
                item.canonicalId = keyId
            }.build()
        )
    }

    //
    // First room area
    //

    areaBuilderService
        .buildRoomFrom("hall1", "killing1", Direction.NORTH) {
            it.name = "The center of a killing room"
        }
        .buildRoom("cage1", Direction.WEST) {
            it.name = "The small cage"
        }
        .buildRoomFrom("killing1", "cage2", Direction.NORTH)
        .buildRoomFrom("killing1", "cage3", Direction.EAST)

    weakMobBuilder(
        "cage1",
        areaBuilderService.itemBuilder(
            "a sub-issue cloak",
            "tbd",
            1.0,
            5,
        ).also { builder ->
            builder.type = ItemType.EQUIPMENT
            builder.position = Position.ABOUT
            builder.material = Material.TEXTILE
            builder.attributes = mapOf(
                Pair(Attribute.AC_SLASH, 1),
            )
        }.build()
    )

    weakMobBuilder(
        "cage2",
        areaBuilderService.itemBuilder(
            "a sub-issue helmet",
            "tbd",
            1.5,
            5,
        ).also { builder ->
            builder.type = ItemType.EQUIPMENT
            builder.position = Position.HEAD
            builder.material = Material.TEXTILE
            builder.attributes = mapOf(
                Pair(Attribute.AC_SLASH, 1),
                Pair(Attribute.AC_PIERCE, 1),
            )
        }.build()
    )

    weakMobBuilder(
        "cage3",
        areaBuilderService.itemBuilder(
            "a sub-issue tunic",
            "tbd",
            2.5,
            5,
        ).also { builder ->
            builder.type = ItemType.EQUIPMENT
            builder.position = Position.TORSO
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

    areaBuilderService
        .buildRoomFrom("hall1", "killing2", Direction.SOUTH) {
            it.name = "The center of a killing room"
        }
        .buildRoom("cage4", Direction.WEST) {
            it.name = "The small cage"
        }
        .buildRoomFrom("killing2", "cage5", Direction.SOUTH)
        .buildRoomFrom("killing2", "cage6", Direction.EAST)

    weakMobBuilder(
        "cage4",
        areaBuilderService.itemBuilder(
            "sub-issue shoes",
            "tbd",
            3.0,
            5,
        ).also { builder ->
            builder.type = ItemType.EQUIPMENT
            builder.position = Position.FEET
            builder.material = Material.TEXTILE
            builder.attributes = mapOf(
                Pair(Attribute.AC_BASH, 1),
                Pair(Attribute.AC_SLASH, 1),
                Pair(Attribute.AC_PIERCE, 1),
            )
        }.build()
    )

    weakMobBuilder(
        "cage5",
        areaBuilderService.itemBuilder(
            "sub-issue arm guards",
            "tbd",
            2.5,
            5,
        ).also { builder ->
            builder.type = ItemType.EQUIPMENT
            builder.position = Position.ARMS
            builder.material = Material.TEXTILE
            builder.attributes = mapOf(
                Pair(Attribute.AC_BASH, 1),
                Pair(Attribute.AC_SLASH, 2),
                Pair(Attribute.AC_PIERCE, 2),
            )
        }.build()
    )

    weakMobBuilder(
        "cage6",
        areaBuilderService.itemBuilder(
            "sub-issue gloves",
            "tbd",
            1.0,
            5,
        ).also { builder ->
            builder.type = ItemType.EQUIPMENT
            builder.position = Position.HANDS
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

    areaBuilderService
        .buildRoomFrom("hall2", "killing3", Direction.NORTH) {
            it.name = "The center of a killing room"
        }
        .buildRoom("cage7", Direction.WEST) {
            it.name = "The small cage"
        }
        .buildRoomFrom("killing3", "cage8", Direction.NORTH)
        .buildRoomFrom("killing3", "cage9", Direction.EAST)

    weakMobBuilder(
        "cage7",
        areaBuilderService.itemBuilder(
            "a sub-issue shield",
            "tbd",
            8.0,
            5,
        ).also { builder ->
            builder.type = ItemType.EQUIPMENT
            builder.position = Position.SHIELD
            builder.material = Material.WOOD
            builder.attributes = mapOf(
                Pair(Attribute.AC_BASH, 5),
                Pair(Attribute.AC_SLASH, 4),
                Pair(Attribute.AC_PIERCE, 4),
            )
        }.build()
    )

    weakMobBuilder(
        "cage8",
        areaBuilderService.itemBuilder(
            "sub-issue leggings",
            "tbd",
            2.5,
            5,
        ).also { builder ->
            builder.type = ItemType.EQUIPMENT
            builder.position = Position.LEGS
            builder.material = Material.TEXTILE
            builder.attributes = mapOf(
                Pair(Attribute.AC_BASH, 1),
                Pair(Attribute.AC_SLASH, 2),
                Pair(Attribute.AC_PIERCE, 2),
            )
        }.build()
    )

    weakMobBuilder(
        "cage9",
        areaBuilderService.itemBuilder(
            "a sub-issue belt",
            "tbd",
            2.0,
            5,
        ).also { builder ->
            builder.type = ItemType.EQUIPMENT
            builder.position = Position.WAIST
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

    areaBuilderService
        .buildRoomFrom("hall2", "killing4", Direction.SOUTH) {
            it.name = "The center of a killing room"
        }
        .buildRoom("cage10", Direction.WEST) {
            it.name = "The small cage"
        }
        .buildRoomFrom("killing4", "cage11", Direction.SOUTH)
        .buildRoomFrom("killing4", "cage12", Direction.EAST)

    weakMobBuilder(
        "cage10",
        areaBuilderService.itemBuilder(
            "sub-issue bracers",
            "tbd",
            2.0,
            5,
        ).also { builder ->
            builder.type = ItemType.EQUIPMENT
            builder.position = Position.WRIST
            builder.material = Material.WOOD
            builder.attributes = mapOf(
                Pair(Attribute.AC_BASH, 1),
                Pair(Attribute.AC_SLASH, 2),
                Pair(Attribute.AC_PIERCE, 2),
            )
        }.build()
    )

    weakMobBuilder(
        "cage11",
        areaBuilderService.itemBuilder(
            "a sub-issue ring",
            "tbd",
            0.5,
            5,
        ).also { builder ->
            builder.type = ItemType.EQUIPMENT
            builder.position = Position.FINGER
            builder.material = Material.COPPER
            builder.attributes = mapOf(
                Pair(Attribute.AC_BASH, 0),
                Pair(Attribute.AC_SLASH, 1),
                Pair(Attribute.AC_PIERCE, 1),
            )
        }.build()
    )

    weakMobBuilder(
        "cage12",
        areaBuilderService.itemBuilder(
            "a floating stone",
            "tbd",
            1.0,
            5,
        ).also { builder ->
            builder.type = ItemType.EQUIPMENT
            builder.position = Position.FLOAT
            builder.material = Material.STONE
            builder.attributes = mapOf(
                Pair(Attribute.AC_MAGIC, 2),
            )
        }.build()
    )
}
