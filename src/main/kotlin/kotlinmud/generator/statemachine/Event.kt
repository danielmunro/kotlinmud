package kotlinmud.generator.statemachine

sealed class Event {
    object OnReadyForBiomes : Event()
    object OnReadyForRooms : Event()
    object OnReadyForJungle : Event()
    object OnReadyForArborealForest : Event()
    object OnReadyForElevation : Event()
    object OnReadyToCreateMobs : Event()
    object OnReadyToCreateExits : Event()
}
