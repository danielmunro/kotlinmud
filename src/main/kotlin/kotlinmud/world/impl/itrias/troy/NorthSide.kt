package kotlinmud.world.impl.itrias.troy

import kotlinmud.item.type.Material
import kotlinmud.item.type.Weapon
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.impl.Kender
import kotlinmud.mob.race.impl.Lasher
import kotlinmud.room.model.Room
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction
import kotlinmud.world.service.AreaBuilderService

fun createTroyNorthSide(areaBuilderService: AreaBuilderService, connection: Room): Room {
    areaBuilderService.startWith(connection)
        .buildRoom("road1", Direction.NORTH) {
            it.name = "Pike Street"
            it.description = "tbd"
        }
        .buildRoom("road2", Direction.NORTH)
        .buildRoom("road3", Direction.NORTH)
        .buildRoom("gate", Direction.NORTH) {
            it.name = "Troy North Gate"
        }
        .startWith("road1")
        .buildRoom(Direction.WEST) {
            it.name = "Troy weapon smith shop"
        }

    areaBuilderService.buildShopkeeper(
        "the weaponsmith",
        "the weaponsmith is here",
        "tbd",
        Kender(),
        mapOf(
            Pair(
                areaBuilderService.buildWeapon(
                    "an iron sword",
                    "tbd",
                    4.0,
                    Weapon.SWORD,
                    DamageType.SLASH,
                    Material.IRON,
                    1,
                    2,
                    10,
                ),
                100
            ),
            Pair(
                areaBuilderService.buildWeapon(
                    "an iron axe",
                    "tbd",
                    4.0,
                    Weapon.AXE,
                    DamageType.SLASH,
                    Material.IRON,
                    1,
                    2,
                    10,
                ),
                100
            ),
            Pair(
                areaBuilderService.buildWeapon(
                    "an iron mace",
                    "tbd",
                    7.0,
                    Weapon.MACE,
                    DamageType.POUND,
                    Material.IRON,
                    1,
                    2,
                    10,
                ),
                100
            ),
            Pair(
                areaBuilderService.buildWeapon(
                    "an iron dagger",
                    "tbd",
                    2.0,
                    Weapon.DAGGER,
                    DamageType.PIERCE,
                    Material.IRON,
                    1,
                    2,
                    10,
                ),
                100
            ),
        ),
    )

    areaBuilderService.startWith("road1")
        .buildRoom(Direction.EAST) {
            it.name = "Armor and shields"
        }

    areaBuilderService.buildShopkeeper(
        "the armorer",
        "the armorer is here",
        "tbd",
        Lasher(),
        mapOf(),
    )

    createMudSchool(areaBuilderService.copy(Area.MudSchool), areaBuilderService.getRoomFromLabel("road2"))

    return areaBuilderService.getRoomFromLabel("gate")
}
