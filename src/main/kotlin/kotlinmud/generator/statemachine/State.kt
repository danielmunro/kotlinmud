package kotlinmud.generator.statemachine

sealed class State {
    object Biomes : State()
    object Elevation : State()
    object CreateRooms : State()
    object CreateJungle : State()
    object CreateArborealForest : State()
    object CreateMobs : State()
    object CreateExits : State()
    object Done : State()
}
