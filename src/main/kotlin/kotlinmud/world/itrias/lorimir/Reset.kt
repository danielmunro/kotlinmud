package kotlinmud.world.itrias.lorimir

import kotlinmud.item.helper.ItemBuilder
import kotlinmud.item.model.ItemRespawn
import kotlinmud.item.service.ItemRespawnService
import kotlinmud.item.type.Food
import kotlinmud.item.type.ItemCanonicalId
import kotlinmud.item.type.Material
import kotlinmud.mob.helper.MobBuilder
import kotlinmud.mob.model.MobRespawn
import kotlinmud.mob.race.impl.Canid
import kotlinmud.mob.service.MobRespawnService
import kotlinmud.mob.type.MobCanonicalId
import kotlinmud.room.type.Area

fun resetForest() {
    ItemRespawnService(
        listOf(
            ItemRespawn(
                ItemCanonicalId.Mushroom,
                ItemBuilder("a small brown mushroom")
                    .description("foo")
                    .material(Material.ORGANIC)
                    .food(Food.MUSHROOM)
                    .canonicalId(ItemCanonicalId.Mushroom),
                Area.LorimirForest,
                10,
            ),
        )
    ).respawn()

    MobRespawnService(
        listOf(
            MobRespawn(
                MobCanonicalId.SmallFox,
                MobBuilder()
                    .name("a small fox")
                    .brief("a small fox darts through the underbrush")
                    .description("a small fox is here.")
                    .level(3)
                    .race(Canid())
                    .canonicalId(MobCanonicalId.SmallFox),
                Area.LorimirForest,
                10
            )
        )
    ).respawn()
}
