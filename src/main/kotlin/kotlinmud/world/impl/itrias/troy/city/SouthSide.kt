package kotlinmud.world.impl.itrias.troy.city

import kotlinmud.mob.race.impl.Dwarf
import kotlinmud.mob.race.impl.Goblin
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.type.JobType
import kotlinmud.room.model.Room
import kotlinmud.room.type.Direction
import kotlinmud.world.factory.createAmberAle
import kotlinmud.world.factory.createCureBlindnessPotion
import kotlinmud.world.factory.createCureLightPotion
import kotlinmud.world.factory.createCurePoisonPotion
import kotlinmud.world.factory.createHastePotion
import kotlinmud.world.factory.createIPA
import kotlinmud.world.factory.createPorter
import kotlinmud.world.factory.createRemoveCursePotion
import kotlinmud.world.service.AreaBuilderService

fun createTroySouthGate(
    areaBuilderService: AreaBuilderService,
    connection: Room,
    toPromenade: Room,
) {
    areaBuilderService.roomBuilder(
        "The City of Troy",
        "tbd",
    )

    areaBuilderService.startWith(connection)
        .buildRoom("road1", Direction.SOUTH) {
            it.name = "South Market Street"
        }
        .buildRoom("road2", Direction.SOUTH)
        .buildRoom("road3", Direction.SOUTH)
        .connectTo(toPromenade, Direction.UP)

    areaBuilderService.startWith("road1")
        .buildRoom(Direction.EAST) {
            it.name = "Potions & Apothecary"
            it.description = "A potion shop."
        }

    areaBuilderService.buildShopkeeper(
        "a potion brewer",
        "a potion brewer stands here",
        "tbd",
        Human(),
        mapOf(
            Pair(createCureLightPotion(areaBuilderService), 100),
            Pair(createCurePoisonPotion(areaBuilderService), 100),
            Pair(createCureBlindnessPotion(areaBuilderService), 100),
            Pair(createRemoveCursePotion(areaBuilderService), 100),
            Pair(createHastePotion(areaBuilderService), 100),
        ),
    )

    areaBuilderService.startWith("road2")
        .buildRoom(Direction.WEST) {
            it.name = "The Ramshackle Tavern"
            it.description =
                "A humble and aging wooden structure surrounds you. Patrons sit around dimly lit tables, swapping tales of yore."
        }

    areaBuilderService.buildShopkeeper(
        "the barkeeper",
        "the barkeeper is here, cleaning out a mug",
        "tbd",
        Human(),
        mapOf(
            Pair(createAmberAle(areaBuilderService), 100),
            Pair(createPorter(areaBuilderService), 100),
            Pair(createIPA(areaBuilderService), 100),
        ),
    )

    areaBuilderService.startWith("road2")
        .buildRoom(Direction.EAST) {
            it.name = "The Bakery"
            it.description = "tbd"
        }

    areaBuilderService.buildShopkeeper(
        "a baker",
        "a baker is here",
        "tbd",
        Human(),
        mapOf(),
    )

    areaBuilderService.startWith("road2")
        .buildRoom(Direction.WEST) {
            it.name = "Wand shop"
            it.description = "tbd"
        }

    areaBuilderService.buildShopkeeper(
        "a wand maker",
        "a wand maker is here",
        "tbd",
        Human(),
        mapOf(),
    )

    areaBuilderService.startWith("road3")
        .buildRoom(Direction.WEST) {
            it.name = "First Bank of Troy"
        }

    areaBuilderService.buildShopkeeper(
        "a banker",
        "a banker is here",
        "tbd",
        Goblin(),
        mapOf(),
    )

    areaBuilderService.startWith("road3")
        .buildRoom(Direction.EAST) {
            it.name = "Inn at Market Street"
        }

    areaBuilderService.buildShopkeeper(
        "the innkeeper",
        "the innkeeper is here",
        "tbd",
        Dwarf(),
        mapOf(),
    )

    areaBuilderService.buildFodder(
        "a Troy city guard",
        "a guard of the city is here, patrolling the streets",
        "tbd",
        Human(),
        10,
        1,
    ).also {
        it.job = JobType.PATROL
        it.randomizeRoom = false
        it.room = areaBuilderService.getRoomFromLabel("road3")
        it.messages = listOf("quiet down!")
    }
}
