package kotlinmud.world

import kotlinmud.room.type.Area
import kotlinmud.world.impl.itrias.ironBluff.createIronBluffMainRoad
import kotlinmud.world.impl.itrias.lorimir.createLorimirForest
import kotlinmud.world.impl.itrias.lorimir.createLorimirForestLake
import kotlinmud.world.impl.itrias.lorimir.createLorimirForestOutpost
import kotlinmud.world.impl.itrias.lorimir.createSouthernTrail
import kotlinmud.world.impl.itrias.troy.city.createTroyTownCenter
import kotlinmud.world.impl.itrias.troy.createTroyOutskirts
import kotlinmud.world.impl.itrias.troy.createTroyPromenade
import kotlinmud.world.service.AreaBuilderService

fun createWorld(areaBuilderServiceFactory: (area: Area) -> AreaBuilderService) {
    val outpost = createLorimirForestOutpost(areaBuilderServiceFactory(Area.LorimirForestOutpost))

    val lorimirForest = createLorimirForest(
        areaBuilderServiceFactory(Area.LorimirForest)
            .startWith(outpost.getValue(Area.LorimirForest)),
    )

    createSouthernTrail(
        areaBuilderServiceFactory(Area.SouthernTrail)
            .startWith(lorimirForest.getValue(Area.SouthernTrail)),
    )

    val outskirtsConnection = createTroyOutskirts(
        areaBuilderServiceFactory(Area.TroyOutskirts),
        outpost.getValue(Area.TroyOutskirts),
    )

    val promenade = createTroyPromenade(
        areaBuilderServiceFactory(Area.TroyPromenade),
        outskirtsConnection.getValue(Area.TroyPromenade),
    )

    val troyExits = createTroyTownCenter(
        areaBuilderServiceFactory(Area.Troy),
        promenade.getValue(Area.Troy),
    )

    createIronBluffMainRoad(
        areaBuilderServiceFactory(Area.IronBluffMainRoad)
            .startWith(troyExits[Area.IronBluffMainRoad]!!)
    )

    createLorimirForestLake(
        areaBuilderServiceFactory(Area.LakeOsona),
        lorimirForest[Area.LakeOsona]!!,
    )
}
