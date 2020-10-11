package kotlinmud.generator.statemachine

sealed class SideEffect {
    object LogTerrainGenerated : SideEffect()
}
