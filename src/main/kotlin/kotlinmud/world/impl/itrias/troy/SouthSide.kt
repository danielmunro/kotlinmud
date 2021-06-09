package kotlinmud.world.impl.itrias.troy

import kotlinmud.mob.race.impl.Dwarf
import kotlinmud.mob.race.impl.Goblin
import kotlinmud.mob.race.impl.Human
import kotlinmud.room.builder.build
import kotlinmud.room.helper.connect
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

fun createTroySouthGate(areaBuilderService: AreaBuilderService, connection: Room,): Room {
    val roomBuilder = areaBuilderService.roomBuilder(
        "The City of Troy",
        "tbd",
    )

    val potionShop = areaBuilderService.buildRoom("potions") {
        it.name = "Potions & Apothecary"
        it.description = "A potion shop."
    }.getLastRoom()

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

    val tavern = areaBuilderService.buildRoom {
        it.name = "The Ramshackle Tavern"
        it.description =
            "A humble and aging wooden structure surrounds you. Patrons sit around dimly lit tables, swapping tales of yore."
    }.getLastRoom()

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

    val marketStreetBuilder = roomBuilder.copy {
        it.name = "South Market Street"
    }

    val main1 = build(marketStreetBuilder)

    val bakery = areaBuilderService.buildRoom {
        it.name = "The Bakery"
        it.description = "tbd"
    }.getLastRoom()

    areaBuilderService.buildShopkeeper(
        "a baker",
        "a baker is here",
        "tbd",
        Human(),
        mapOf(),
    )

    val wandStore = areaBuilderService.buildRoom {
        it.name = "Wand shop"
        it.description = "tbd"
    }.getLastRoom()

    areaBuilderService.buildShopkeeper(
        "a wand maker",
        "a wand maker is here",
        "tbd",
        Human(),
        mapOf(),
    )

    val main2 = build(marketStreetBuilder)

    val bank = areaBuilderService.buildRoom {
        it.name = "First Bank of Troy"
    }.getLastRoom()

    areaBuilderService.buildShopkeeper(
        "a banker",
        "a banker is here",
        "tbd",
        Goblin(),
        mapOf(),
    )

    val inn = areaBuilderService.buildRoom {
        it.name = "Inn at Market Street"
    }.getLastRoom()

    areaBuilderService.buildShopkeeper(
        "the innkeeper",
        "the innkeeper is here",
        "tbd",
        Dwarf(),
        mapOf(),
    )

    val main3 = build(marketStreetBuilder)

    connect(main2)
        .toRoom(main3, Direction.NORTH)
        .toRoom(
            listOf(
                Pair(bank, Direction.WEST),
                Pair(inn, Direction.EAST),
            )
        )

    connect(main1)
        .toRoom(main2, Direction.NORTH)
        .toRoom(
            listOf(
                Pair(bakery, Direction.WEST),
                Pair(wandStore, Direction.EAST),
            )
        )

    connect(connection)
        .toRoom(main1, Direction.DOWN)
        .toRoom(
            listOf(
                Pair(potionShop, Direction.WEST),
                Pair(tavern, Direction.EAST),
            )
        )

    return main3
}
