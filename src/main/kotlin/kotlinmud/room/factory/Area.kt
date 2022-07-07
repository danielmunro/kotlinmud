package kotlinmud.room.factory

import kotlinmud.room.model.Area
import kotlinmud.room.type.Lighting

fun createPurgatoryArea(): Area {
    return Area(
        0,
        "Purgatory",
        Lighting.Poor,
    )
}

fun createTestArea(): Area {
    return Area(
        0,
        "Test",
        Lighting.Moderate,
    )
}

fun createSecondTestArea(): Area {
    return Area(
        0,
        "Second Test",
        Lighting.Moderate,
    )
}

fun createMidgaardArea(): Area {
    return Area(
        1,
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
