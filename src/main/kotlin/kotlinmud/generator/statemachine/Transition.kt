package kotlinmud.generator.statemachine

import com.tinder.StateMachine
import kotlinmud.biome.helper.createBiomes
import kotlinmud.generator.config.GeneratorConfig
import kotlinmud.generator.model.World
import kotlinmud.generator.service.BiomeService
import kotlinmud.generator.service.CreateRoomService
import kotlinmud.generator.service.ElevationService
import kotlinmud.generator.service.ExitCreationService
import kotlinmud.generator.service.MobGeneratorService
import kotlinmud.generator.service.WorldGeneration
import kotlinmud.generator.type.WorldGeneratorStateMachine

fun createStateMachine(
    config: GeneratorConfig,
    biomeService: BiomeService,
    worldGeneration: WorldGeneration
): WorldGeneratorStateMachine {
    return StateMachine.create {
        initialState(State.Biomes)
        val biomes = createBiomes()
        val createRoomService = CreateRoomService()
        state<State.Biomes> {
            on<Event.OnReadyForBiomes> {
                worldGeneration.biomeLayer =
                    biomeService.createLayer((config.width * config.length) / (config.width * config.length / 10))
                transitionTo(State.Elevation)
            }
        }
        state<State.Elevation> {
            on<Event.OnReadyForElevation> {
                worldGeneration.elevationLayer =
                    ElevationService(worldGeneration.biomeLayer!!, biomes).buildLayer()
                transitionTo(State.CreateRooms)
            }
        }
        state<State.CreateRooms> {
            on<Event.OnReadyForRooms> {
                worldGeneration.matrix =
                    createRoomService.generate(config, worldGeneration.elevationLayer!!, worldGeneration.biomeLayer!!)
                transitionTo(State.CreateMobs)
            }
        }
        state<State.CreateMobs> {
            on<Event.OnReadyToCreateMobs> {
                MobGeneratorService(biomes).respawnMobs()
                transitionTo(State.CreateExits)
            }
        }
        state<State.CreateExits> {
            on<Event.OnReadyToCreateExits> {
                worldGeneration.world = World(
                    createRoomService.rooms,
                    worldGeneration.matrix!!
                )
                ExitCreationService(worldGeneration.world!!).hookUpRoomExits()
                transitionTo(State.Done)
            }
        }
        state<State.Done> {
        }
    }
}

fun runStateMachine(stateMachine: WorldGeneratorStateMachine) {
    stateMachine.transition(Event.OnReadyForBiomes)
    stateMachine.transition(Event.OnReadyForElevation)
    stateMachine.transition(Event.OnReadyForRooms)
    stateMachine.transition(Event.OnReadyToCreateMobs)
    stateMachine.transition(Event.OnReadyToCreateExits)
}
