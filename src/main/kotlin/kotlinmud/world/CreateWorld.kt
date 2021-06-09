package kotlinmud.world

import kotlinmud.room.type.Area
import kotlinmud.world.impl.itrias.lorimir.createLorimirForest
import kotlinmud.world.impl.itrias.lorimir.createLorimirForestLake
import kotlinmud.world.impl.itrias.lorimir.createLorimirForestOutpost
import kotlinmud.world.impl.itrias.troy.createTroyOutskirts
import kotlinmud.world.impl.itrias.troy.createTroyPromenade
import kotlinmud.world.impl.itrias.troy.createTroyTownCenter
import kotlinmud.world.service.AreaBuilderService

fun createWorld(areaBuilderServiceFactory: (area: Area) -> AreaBuilderService) {
    val trailNearCamp = createLorimirForestOutpost(areaBuilderServiceFactory(Area.LorimirForestOutpost))

    val lorimirForest = createLorimirForest(
        areaBuilderServiceFactory(Area.LorimirForest),
        trailNearCamp,
    )

    val outskirtsConnection = createTroyOutskirts(
        areaBuilderServiceFactory(Area.TroyOutskirts),
        lorimirForest,
    )

    val promenade = createTroyPromenade(
        areaBuilderServiceFactory(Area.TroyPromenade),
        outskirtsConnection,
    )

    createTroyTownCenter(
        areaBuilderServiceFactory(Area.Troy),
        promenade,
    )

    createLorimirForestLake(
        areaBuilderServiceFactory(Area.LakeOsona),
        lorimirForest,
    )
}
