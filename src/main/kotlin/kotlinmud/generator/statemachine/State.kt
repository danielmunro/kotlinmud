package kotlinmud.generator.statemachine

sealed class State {
    object Init : State()
    object Biomes : State()
    object Elevation : State()
    object CreateRooms : State()
    object CreateMobs : State()
    object CreateExits : State()
    object Done : State()
}
