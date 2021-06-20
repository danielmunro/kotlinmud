package kotlinmud.world.impl.itrias.ironBluff

import kotlinmud.generator.service.SimpleMatrixService
import kotlinmud.mob.race.impl.Cow
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.race.impl.Rodent
import kotlinmud.mob.race.impl.Sheep
import kotlinmud.mob.type.QuestGiver
import kotlinmud.quest.type.QuestType
import kotlinmud.resource.impl.CowHide
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction
import kotlinmud.world.service.AreaBuilderService

fun createPyreforgeFarm(areaBuilderService: AreaBuilderService) {
    val matrixBuilder = { area: Area, name: String ->
        SimpleMatrixService(
            areaBuilderService.roomBuilder(
                name,
                "A field on Pyreforge farm.",
            ).also {
                it.area = area
            }
        ).build(3, 3)
    }
    areaBuilderService.buildRoom(Direction.NORTH) {
        it.name = "Main gate for Pyreforge farm estate"
    }
        .buildRoom("road1", Direction.NORTH) {
            it.name = "A dusty dirt road"
        }
        .connectTo(
            matrixBuilder(Area.PyreforgeSheepField, "A field for sheep")[2][2],
            Direction.WEST,
        )
        .startWith("road1")
        .connectTo(
            matrixBuilder(Area.PyreforgeCowField, "A field for cows")[0][0],
            Direction.EAST,
        )
        .startWith("road1")
        .buildRoom("road2", Direction.NORTH)
        .connectTo(
            matrixBuilder(Area.PyreforgeWheatField, "A wheat field")[0][2],
            Direction.WEST,
        )
        .startWith("road2")
        .connectTo(
            matrixBuilder(Area.PyreforgePumpkinField, "A pumpkin field")[2][0],
            Direction.EAST,
        )
        .startWith("road2")
        .buildRoom(Direction.NORTH) {
            it.name = "Pyreforge Manor"
        }

    areaBuilderService.buildQuestGiver(
        "lady Beatrice Pyreforge",
        "lady Beatrice Pyreforge, Pyreforge matriarch",
        "tbd",
        Human(),
        QuestGiver.BeatricePyreforge,
    )

    areaBuilderService.switchArea(Area.PyreforgeSheepField).buildFodder(
        "a sheep",
        "a sheep is here, wondering if you have a tasty snack",
        "tbd",
        Sheep(),
        8,
        3,
    )

    areaBuilderService.switchArea(Area.PyreforgeCowField).buildFodder(
        "a cow",
        "a cow is here, grazing idly",
        "tbd",
        Cow(),
        8,
        3,
    ).also {
        it.resources = listOf(CowHide())
    }

    areaBuilderService.switchArea(Area.PyreforgeWheatField).buildFodder(
        "a field mouse",
        "a harmless field mouse is here, scurrying away",
        "tbd",
        Rodent(),
        3,
        8,
    ).also {
        it.partOfQuest = QuestType.CLEAR_PYREFORGE_RODENTS
    }

    areaBuilderService.switchArea(Area.PyreforgePumpkinField).buildFodder(
        "a giant rat",
        "a giant rat is here, eating a smashed pumpkin",
        "tbd",
        Rodent(),
        5,
        6,
    ).also {
        it.partOfQuest = QuestType.CLEAR_PYREFORGE_RODENTS
    }
}
