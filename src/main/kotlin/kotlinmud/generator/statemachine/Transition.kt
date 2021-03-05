package kotlinmud.generator.statemachine

import com.tinder.StateMachine
import kotlinmud.biome.helper.createBiomes
import kotlinmud.generator.config.GeneratorConfig
import kotlinmud.generator.service.BiomeService
import kotlinmud.generator.service.CreateRoomService
import kotlinmud.generator.service.WorldGeneration
import kotlinmud.generator.statemachine.transition.biomeTransition
import kotlinmud.generator.statemachine.transition.createArborealForestTransition
import kotlinmud.generator.statemachine.transition.createExitTransition
import kotlinmud.generator.statemachine.transition.createJungleTransition
import kotlinmud.generator.statemachine.transition.createMobsTransition
import kotlinmud.generator.statemachine.transition.createRoomsTransition
import kotlinmud.generator.statemachine.transition.elevationTransition
import kotlinmud.generator.type.WorldGeneratorStateMachine
import kotlinmud.room.service.RoomService

fun createStateMachine(
    config: GeneratorConfig,
    roomService: RoomService,
    biomeService: BiomeService,
    worldGeneration: WorldGeneration
): WorldGeneratorStateMachine {
    return StateMachine.create {
        initialState(State.Biomes)
        val biomes = createBiomes()
        val createRoomService = CreateRoomService(roomService)
        state<State.Biomes> {
            on<Event.OnReadyForBiomes> {
                biomeTransition(worldGeneration, biomeService, config)
                transitionTo(State.Elevation)
            }
        }
        state<State.Elevation> {
            on<Event.OnReadyForElevation> {
                elevationTransition(worldGeneration, biomes)
                transitionTo(State.CreateRooms)
            }
        }
        state<State.CreateRooms> {
            on<Event.OnReadyForRooms> {
                createRoomsTransition(worldGeneration, createRoomService, config)
                transitionTo(State.CreateArborealForest)
            }
        }
        state<State.CreateArborealForest> {
            on<Event.OnReadyForArborealForest> {
                createArborealForestTransition(roomService)
                transitionTo(State.CreateJungle)
            }
        }
        state<State.CreateJungle> {
            on<Event.OnReadyForJungle> {
                createJungleTransition(roomService)
                transitionTo(State.CreateMobs)
            }
        }
        state<State.CreateMobs> {
            on<Event.OnReadyToCreateMobs> {
                createMobsTransition(biomes)
                transitionTo(State.CreateExits)
            }
        }
        state<State.CreateExits> {
            on<Event.OnReadyToCreateExits> {
                createExitTransition(worldGeneration, createRoomService)
                transitionTo(State.Done)
            }
        }
        state<State.Done> {}
    }
}

fun runStateMachine(stateMachine: WorldGeneratorStateMachine) {
    stateMachine.transition(Event.OnReadyForBiomes)
    stateMachine.transition(Event.OnReadyForElevation)
    stateMachine.transition(Event.OnReadyForRooms)
    stateMachine.transition(Event.OnReadyForArborealForest)
    stateMachine.transition(Event.OnReadyForJungle)
    stateMachine.transition(Event.OnReadyToCreateMobs)
    stateMachine.transition(Event.OnReadyToCreateExits)
}
