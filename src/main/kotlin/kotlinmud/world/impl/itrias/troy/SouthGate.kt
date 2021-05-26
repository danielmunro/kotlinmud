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
        it.canonicalId = MobCanonicalId.PotionBrewer
        it.job = JobType.SHOPKEEPER
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
        it.canonicalId = MobCanonicalId.Barkeeper
        it.job = JobType.SHOPKEEPER
    }

    itemRespawnsFor(
        MobCanonicalId.Barkeeper,
        listOf(
            Pair(createAmberAle(itemService), 100),
            Pair(createPorter(itemService), 100),
            Pair(createIPA(itemService), 100),
        )
    )

    val main1 = build(roomBuilder)

    connect(connection)
        .toRoom(main1)
        .toRoom(listOf(
            Pair(potionShop, Direction.WEST),
            Pair(tavern, Direction.EAST),
        ))

    return main1
}
