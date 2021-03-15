package kotlinmud.world.itrias.lorimir

import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.model.ItemAreaRespawn
import kotlinmud.item.model.ItemMobRespawn
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.Food
import kotlinmud.item.type.ItemCanonicalId
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.mob.builder.MobBuilder
import kotlinmud.mob.model.MobRespawn
import kotlinmud.mob.race.impl.Canid
import kotlinmud.mob.race.impl.Ogre
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.MobCanonicalId
import kotlinmud.room.type.Area

fun getLorimirItemMobRespawns(itemService: ItemService): List<ItemMobRespawn> {
    return listOf(
        ItemMobRespawn(
            ItemCanonicalId.Bread,
            ItemBuilder(itemService).also {
                it.type = ItemType.FOOD
                it.worth = 10
                it.name = "a small hard loaf of bread"
                it.description = "foo"
                it.material = Material.ORGANIC
                it.food = Food.BREAD
                it.canonicalId = ItemCanonicalId.Bread
            },
            MobCanonicalId.Barbosa,
            100,
        )
    )
}

fun getLorimirItemAreaRespawns(itemService: ItemService): List<ItemAreaRespawn> {
    return listOf(
        ItemAreaRespawn(
            ItemCanonicalId.Mushroom,
            ItemBuilder(itemService)
                .name("a small brown mushroom")
                .description("foo")
                .material(Material.ORGANIC)
                .food(Food.MUSHROOM)
                .canonicalId(ItemCanonicalId.Mushroom),
            Area.LorimirForest,
            10,
        ),
    )
}

fun getLorimirMobRespawns(mobService: MobService): List<MobRespawn> {
    return listOf(
        MobRespawn(
            MobCanonicalId.SmallFox,
            MobBuilder(mobService).also {
                it.name = "a small fox"
                it.brief = "a small fox darts through the underbrush"
                it.description = "a small fox is here."
                it.level = 3
                it.race = Canid()
                it.canonicalId = MobCanonicalId.SmallFox
            },
            Area.LorimirForest,
            10
        ),
        MobRespawn(
            MobCanonicalId.Grongok,
            MobBuilder(mobService).also {
                it.name = "Grongok"
                it.brief = "a wild looking ogre is here"
                it.description = "Grongok the wild is here."
                it.level = 3
                it.race = Ogre()
                it.canonicalId = MobCanonicalId.Grongok
            },
            Area.GrongokHideout,
            1
        ),
    )
}
