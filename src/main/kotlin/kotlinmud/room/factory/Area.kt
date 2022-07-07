package kotlinmud.room.factory

import kotlinmud.room.model.Area

fun createPurgatoryArea(): Area {
    return Area(
        0,
        "Purgatory",
    )
}

fun createTestArea(): Area {
    return Area(
        0,
        "Test",
    )
}

fun createSecondTestArea(): Area {
    return Area(
        0,
        "Second Test",
    )
}

fun createMidgaardArea(): Area {
    return Area(
        1,
        "Midgaard",
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
