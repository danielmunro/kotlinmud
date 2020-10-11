package kotlinmud.generator.statemachine

sealed class Event {
    object OnReadyToStart : Event()
    object OnTerrainGenerated : Event()
    object OnReadyForBiomes : Event()
    object OnReadyForRooms : Event()
    object OnReadyForElevation : Event()
    object OnReadyToCreateMobs : Event()
    object OnReadyToCreateExits : Event()
}
