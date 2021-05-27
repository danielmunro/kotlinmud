package kotlinmud.world.impl.itrias.troy

import kotlinmud.item.service.ItemService
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.JobType
import kotlinmud.mob.type.MobCanonicalId
import kotlinmud.respawn.helper.itemRespawnsFor
import kotlinmud.room.builder.build
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction
import kotlinmud.world.factory.createAmberAle
import kotlinmud.world.factory.createCureBlindnessPotion
import kotlinmud.world.factory.createCureLightPotion
import kotlinmud.world.factory.createCurePoisonPotion
import kotlinmud.world.factory.createHastePotion
import kotlinmud.world.factory.createIPA
import kotlinmud.world.factory.createPorter
import kotlinmud.world.factory.createRemoveCursePotion

fun createTroySouthGate(
    mobService: MobService,
    roomService: RoomService,
    itemService: ItemService,
    connection: Room,
): Room {
    val roomBuilder = roomService.builder(
        "The City of Troy",
        "tbd",
        Area.Troy,
    )

    val potionShop = build(
        roomBuilder.copy {
            it.name = "Potions & Apothecary"
            it.description = "A potion shop."
        }
    )

    mobService.builder(
        "a potion brewer",
        "a potion brewer stands here",
        "tbd",
    ).also {
        it.room = potionShop
        it.makeShopkeeper(MobCanonicalId.PotionBrewer)
    }.build()

    itemRespawnsFor(
        MobCanonicalId.PotionBrewer,
        listOf(
            Pair(createCureLightPotion(itemService), 100),
            Pair(createCurePoisonPotion(itemService), 100),
            Pair(createCureBlindnessPotion(itemService), 100),
            Pair(createRemoveCursePotion(itemService), 100),
            Pair(createHastePotion(itemService), 100),
        )
    )

    val tavern = build(
        roomBuilder.copy {
            it.name = "The Ramshackle Tavern"
            it.description =
                "A humble and aging wooden structure surrounds you. Patrons sit around dimly lit tables, swapping tales of yore."
        }
    )

    mobService.builder(
        "the barkeeper",
        "the barkeeper is here, cleaning out a mug",
        "tbd",
    ).also {
        it.room = tavern
        it.makeShopkeeper(MobCanonicalId.Barkeeper)
    }

    itemRespawnsFor(
        MobCanonicalId.Barkeeper,
        listOf(
            Pair(createAmberAle(itemService), 100),
            Pair(createPorter(itemService), 100),
            Pair(createIPA(itemService), 100),
        )
    )

    val marketStreet = roomBuilder.copy {
        it.name = "South Market Street"
    }

    val main1 = build(marketStreet)

    val bakery = build(roomBuilder.copy {
        it.name = "The Bakery"
        it.description = "tbd"
    })

    mobService.builder(
        "a baker",
        "a baker is here",
        "tbd"
    ).also {
        it.room = bakery
        it.makeShopkeeper(MobCanonicalId.Baker)
    }.build()

    val wandStore = build(roomBuilder.copy {
        it.name = "Wand shop"
        it.description = "tbd"
    })

    mobService.builder(
        "a wand maker",
        "a wand maker is here",
        "tbd"
    ).also {
        it.room = wandStore
        it.makeShopkeeper(MobCanonicalId.WandMaker)
    }.build()

    val main2 = build(marketStreet)

    val bank = build(roomBuilder.copy {
        it.name = "First Bank of Troy"
    })

    mobService.builder(
        "a banker",
        "a banker is here",
        "tbd"
    ).also {
        it.room = bank
        it.makeShopkeeper(MobCanonicalId.Banker)
    }.build()

    val inn = build(roomBuilder.copy {
        it.name = "Inn at Market Street"
    })

    mobService.builder(
        "the innkeeper",
        "the innkeeper is here",
        "tbd"
    ).also {
        it.room = inn
        it.makeShopkeeper(MobCanonicalId.Innkeeper)
    }.build()

    val main3 = build(marketStreet)

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
        .toRoom(listOf(
            Pair(potionShop, Direction.WEST),
            Pair(tavern, Direction.EAST),
        ))

    return main3
}
