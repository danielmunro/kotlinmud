package kotlinmud.room.factory

import kotlinmud.room.model.Area
import kotlinmud.room.type.Lighting

fun createPurgatoryArea(): Area {
    return Area(
        1,
        "Purgatory",
        Lighting.Poor,
    )
}

fun createTestArea(): Area {
    return Area(
        2,
        "Test",
        Lighting.Good,
    )
}

fun createSecondTestArea(): Area {
    return Area(
        3,
        "Second Test",
        Lighting.Moderate,
    )
}

fun createMidgaardArea(): Area {
    return Area(
        4,
        "Midgaard",
        Lighting.Good,
    )
}

fun createInitialAreas(): List<Area> {
    return listOf(
        createPurgatoryArea(),
        createTestArea(),
        createSecondTestArea(),
        createMidgaardArea(),
    )
}
